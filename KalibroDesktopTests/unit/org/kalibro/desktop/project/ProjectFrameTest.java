package org.kalibro.desktop.project;

import static org.junit.Assert.*;
import static org.kalibro.core.model.ProjectFixtures.*;
import static org.powermock.api.mockito.PowerMockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.Kalibro;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.model.Project;
import org.kalibro.core.persistence.dao.ConfigurationDao;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.*")
@PrepareOnlyThisForTest({Kalibro.class, ProjectFrame.class})
public class ProjectFrameTest extends KalibroTestCase {

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
		mockStatic(Kalibro.class);
		when(Kalibro.getConfigurationDao()).thenReturn(mock(ConfigurationDao.class));

		panel = spy(new ProjectPanel());
		whenNew(ProjectPanel.class).withNoArguments().thenReturn(panel);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void titleShouldHaveProjectName() {
		assertEquals(project.getName() + " - Project", frame.getTitle());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldShowProjectOnPanel() {
		Mockito.verify(panel).set(project);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRetrieveProject() {
		when(panel.get()).thenReturn(project);
		assertSame(project, frame.get());
	}
}