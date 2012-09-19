package org.kalibro.desktop.project;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import javax.swing.JDesktopPane;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.TestCase;
import org.kalibro.core.model.Project;
import org.kalibro.dao.DaoFactory;
import org.kalibro.dao.ProjectDao;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ProjectController.class, DaoFactory.class})
public class ProjectControllerTest extends TestCase {

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
		mockStatic(DaoFactory.class);
		when(DaoFactory.getProjectDao()).thenReturn(projectDao);
	}

	@Test
	public void checkDesktopPane() {
		assertSame(desktopPane, Whitebox.getInternalState(controller, JDesktopPane.class));
	}

	@Test
	public void checkEntityName() {
		assertEquals("Project", Whitebox.getInternalState(controller, String.class));
	}

	@Test
	public void shouldCreateProject() {
		project = controller.createEntity(NAME);
		assertEquals(NAME, project.getName());
	}

	@Test
	public void shouldGetProjectNamesFromDao() {
		when(projectDao.getProjectNames()).thenReturn(NAMES);
		assertSame(NAMES, controller.getEntityNames());
	}

	@Test
	public void shouldGetProjectFromDao() {
		when(projectDao.getProject(NAME)).thenReturn(project);
		assertSame(project, controller.getEntity(NAME));
	}

	@Test
	public void shouldCreateFrame() throws Exception {
		ProjectFrame projectFrame = mock(ProjectFrame.class);
		whenNew(ProjectFrame.class).withArguments(project).thenReturn(projectFrame);
		assertSame(projectFrame, controller.createFrameFor(project));
	}

	@Test
	public void shouldRemoveProject() {
		controller.removeEntity(NAME);
		Mockito.verify(projectDao).removeProject(NAME);
	}

	@Test
	public void shouldSaveProject() {
		controller.save(project);
		Mockito.verify(projectDao).save(project);
	}

	@Test
	public void shouldSetProjectName() {
		controller.setEntityName(project, NAME);
		Mockito.verify(project).setName(NAME);
	}
}