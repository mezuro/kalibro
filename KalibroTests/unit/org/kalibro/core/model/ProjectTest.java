package org.kalibro.core.model;

import static org.junit.Assert.*;
import static org.kalibro.core.model.ProjectFixtures.newHelloWorld;
import static org.kalibro.core.model.enums.ProjectState.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.KalibroSettings;
import org.kalibro.TestCase;
import org.kalibro.core.concurrent.VoidTask;
import org.kalibro.core.model.enums.ProjectState;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(KalibroSettings.class)
public class ProjectTest extends TestCase {

	private KalibroSettings settings;
	private Project project = newHelloWorld();

	@Before
	public void setUp() {
		settings = new KalibroSettings();
		mockStatic(KalibroSettings.class);
		when(KalibroSettings.load()).thenReturn(settings);
	}

	@Test
	public void checkInitializationAttributes() {
		project = new Project();
		assertNull(project.getId());
		assertEquals("", project.getName());
		assertEquals("", project.getLicense());
		assertEquals("", project.getDescription());
		assertDeepEquals(new Repository(), project.getRepository());
		assertEquals("", project.getConfigurationName());
		assertEquals(NEW, project.getState());
		assertNoError();
	}

	@Test
	public void toStringShouldBeProjectName() {
		assertEquals(project.getName(), "" + project);
	}

	@Test
	public void shouldLoadRepository() {
		Repository repository = PowerMockito.mock(Repository.class);
		project.setRepository(repository);
		project.load();
		Mockito.verify(repository).load(project.getDirectory());
	}

	@Test
	public void shouldBeInErrorStateAfterSettingError() {
		Exception error = new Exception();
		project.setError(error);
		assertSame(error, project.getError());
		assertEquals(ERROR, project.getState());
	}

	@Test
	public void shouldGetStateMessageFromState() {
		for (ProjectState state : ProjectState.values()) {
			setState(state);
			assertEquals(state.getMessage(project.getName()), project.getStateMessage());
		}
	}

	private void setState(ProjectState state) {
		if (state == ERROR)
			project.setError(new Exception());
		else
			project.setState(state);
	}

	@Test
	public void shouldRetrieveStateWhenErrorOcurred() {
		List<ProjectState> normalStates = new ArrayList<ProjectState>(Arrays.asList(ProjectState.values()));
		normalStates.remove(ERROR);
		for (ProjectState state : normalStates) {
			project.setState(state);
			project.setError(new Exception());
			assertEquals(state, project.getStateWhenErrorOcurred());
		}
	}

	@Test
	public void shouldThrowExceptionWhenGettingStateWhenErrorOcurredWithoutError() {
		assertThat(new VoidTask() {

			@Override
			protected void perform() {
				project.getStateWhenErrorOcurred();
			}
		}).throwsException().withMessage("Project " + project + " has no error");
	}

	@Test
	public void shouldNotAllowErrorStateWithoutException() {
		assertThat(new VoidTask() {

			@Override
			protected void perform() {
				project.setState(ERROR);
			}
		}).throwsException().withMessage("Use setError(Throwable) to put project in error state");
	}

	@Test
	public void shouldClearErrorWhenSettingNormalState() {
		project.setError(new Exception());
		project.setState(ANALYZING);
		assertNoError();
	}

	private void assertNoError() {
		assertThat(new VoidTask() {

			@Override
			protected void perform() {
				project.getError();
			}
		}).throwsException().withMessage("Project " + project + " has no error");
	}

	@Test
	public void shouldRetrieveProjectDirectory() {
		project.setId(42L);
		project.setName("Testing camel case++");
		assertEquals("42-testingCamelCase", project.getDirectory().getName());
		assertEquals(settings.getServerSettings().getLoadDirectory(), project.getDirectory().getParentFile());
	}

	@Test
	public void shouldSortByName() {
		assertSorted(newProject(""), newProject("Abc"), newProject("Def"), newProject("Xyz"));
	}

	private Project newProject(String name) {
		Project newProject = new Project();
		newProject.setName(name);
		return newProject;
	}
}