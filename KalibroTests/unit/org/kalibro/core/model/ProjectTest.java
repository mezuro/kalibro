package org.kalibro.core.model;

import static org.junit.Assert.*;
import static org.kalibro.core.model.enums.ProjectState.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.concurrent.Task;
import org.kalibro.core.model.enums.ProjectState;

public class ProjectTest extends KalibroTestCase {

	private Project project;

	@Before
	public void setUp() {
		project = ProjectFixtures.helloWorld();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkInitializationAttributes() {
		project = new Project();
		assertEquals("", project.getName());
		assertEquals("", project.getLicense());
		assertEquals("", project.getDescription());
		assertDeepEquals(new Repository(), project.getRepository());
		assertEquals("", project.getConfigurationName());
		assertEquals(NEW, project.getState());
		assertNoError();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void toStringShouldBeProjectName() {
		assertEquals(project.getName(), "" + project);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldGetLoadCommandsFromRepository() {
		String helloWorldPath = HELLO_WORLD_DIRECTORY.getAbsolutePath();
		List<String> expected = project.getRepository().getLoadCommands(helloWorldPath);
		assertDeepEquals(expected, project.getLoadCommands(helloWorldPath));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldBeInErrorStateAfterSettingError() {
		Exception error = new Exception();
		project.setError(error);
		assertSame(error, project.getError());
		assertEquals(ERROR, project.getState());
	}

	@Test(timeout = UNIT_TIMEOUT)
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

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRetrieveStateWhenErrorOcurred() {
		List<ProjectState> normalStates = new ArrayList<ProjectState>(Arrays.asList(ProjectState.values()));
		normalStates.remove(ERROR);
		for (ProjectState state : normalStates) {
			project.setState(state);
			project.setError(new Exception());
			assertEquals(state, project.getStateWhenErrorOcurred());
		}
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldThrowExceptionWhenGettingStateWhenErrorOcurredWithoutError() {
		checkException(new Task() {

			@Override
			public void perform() {
				project.getStateWhenErrorOcurred();
			}
		}, IllegalStateException.class, "Project " + project + " has no error.");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotAllowErrorStateWithoutException() {
		checkException(new Task() {

			@Override
			public void perform() {
				project.setState(ERROR);
			}
		}, IllegalArgumentException.class, "Cannot set error state without exception. Use setError(exception)");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldClearErrorWhenSettingNormalState() {
		project.setError(new Exception());
		project.setState(ANALYZING);
		assertNoError();
	}

	private void assertNoError() {
		checkException(new Task() {

			@Override
			public void perform() {
				project.getError();
			}
		}, IllegalStateException.class, "Project " + project + " has no error.");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSortByName() {
		assertSorted(newProject(""), newProject("Abc"), newProject("Def"), newProject("Xyz"));
	}

	private Project newProject(String name) {
		Project newProject = new Project();
		newProject.setName(name);
		return newProject;
	}
}