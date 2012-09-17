package org.kalibro.desktop.project;

import static org.junit.Assert.*;
import static org.kalibro.core.model.ProjectFixtures.helloWorld;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.TestCase;
import org.kalibro.core.model.Project;
import org.kalibro.dao.ConfigurationDao;
import org.kalibro.dao.DaoFactory;
import org.kalibro.dao.ProjectDao;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.*")
@PrepareOnlyThisForTest({DaoFactory.class, ProjectFrame.class})
public class ProjectFrameTest extends TestCase {

	private Project project;
	private ProjectPanel panel;

	private ProjectFrame frame;

	@Before
	public void setUp() throws Exception {
		project = helloWorld();
		mockPanel();
		frame = new ProjectFrame(project);
	}

	private void mockPanel() throws Exception {
		mockStatic(DaoFactory.class);
		when(DaoFactory.getProjectDao()).thenReturn(mock(ProjectDao.class));
		when(DaoFactory.getConfigurationDao()).thenReturn(mock(ConfigurationDao.class));

		panel = spy(new ProjectPanel());
		whenNew(ProjectPanel.class).withNoArguments().thenReturn(panel);
	}

	@Test
	public void titleShouldHaveProjectName() {
		assertEquals(project.getName() + " - Project", frame.getTitle());
	}

	@Test
	public void shouldShowProjectOnPanel() {
		Mockito.verify(panel).set(project);
	}

	@Test
	public void shouldRetrieveProject() {
		when(panel.get()).thenReturn(project);
		assertSame(project, frame.get());
	}
}