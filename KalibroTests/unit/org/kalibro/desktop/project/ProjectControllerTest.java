package org.kalibro.desktop.project;

import static org.junit.Assert.*;
import static org.powermock.api.mockito.PowerMockito.*;

import java.util.Arrays;
import java.util.List;

import javax.swing.JDesktopPane;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.Kalibro;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.model.Project;
import org.kalibro.core.persistence.dao.ProjectDao;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ProjectController.class, Kalibro.class})
public class ProjectControllerTest extends KalibroTestCase {

	private static final String NAME = "ProjectControllerTest name";
	private static final List<String> NAMES = Arrays.asList(NAME);

	private JDesktopPane desktopPane;
	private Project project;
	private ProjectDao projectDao;

	private ProjectController controller;

	@Before
	public void setUp() {
		desktopPane = mock(JDesktopPane.class);
		project = mock(Project.class);
		mockProjectDao();
		controller = new ProjectController(desktopPane);
	}

	private void mockProjectDao() {
		projectDao = mock(ProjectDao.class);
		mockStatic(Kalibro.class);
		when(Kalibro.getProjectDao()).thenReturn(projectDao);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkDesktopPane() {
		assertSame(desktopPane, Whitebox.getInternalState(controller, JDesktopPane.class));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkEntityName() {
		assertEquals("Project", Whitebox.getInternalState(controller, String.class));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldCreateProject() {
		project = controller.createEntity(NAME);
		assertEquals(NAME, project.getName());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldGetProjectNamesFromDao() {
		when(projectDao.getProjectNames()).thenReturn(NAMES);
		assertSame(NAMES, controller.getEntityNames());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldGetProjectFromDao() {
		when(projectDao.getProject(NAME)).thenReturn(project);
		assertSame(project, controller.getEntity(NAME));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldCreateFrame() throws Exception {
		ProjectFrame projectFrame = mock(ProjectFrame.class);
		whenNew(ProjectFrame.class).withArguments(project).thenReturn(projectFrame);
		assertSame(projectFrame, controller.createFrameFor(project));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRemoveProject() {
		controller.removeEntity(NAME);
		Mockito.verify(projectDao).removeProject(NAME);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSaveProject() {
		controller.save(project);
		Mockito.verify(projectDao).save(project);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSetProjectName() {
		controller.setEntityName(project, NAME);
		Mockito.verify(project).setName(NAME);
	}
}