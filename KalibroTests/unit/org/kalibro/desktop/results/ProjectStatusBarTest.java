package org.kalibro.desktop.results;

import static org.junit.Assert.*;
import static org.kalibro.core.model.ProjectFixtures.*;
import static org.powermock.api.mockito.PowerMockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.Kalibro;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.model.Project;
import org.kalibro.core.model.enums.ProjectState;
import org.kalibro.desktop.swingextension.Label;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.*")
@PrepareForTest(Kalibro.class)
public class ProjectStatusBarTest extends KalibroTestCase {

	private Project project;
	private ProjectStatusBar statusBar;

	@Before
	public void setUp() {
		mockStatic(Kalibro.class);
		project = helloWorld();
		statusBar = new ProjectStatusBar(project);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldListenToProject() {
		verifyStatic();
		Kalibro.addProjectStateListener(project, statusBar);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldChangeMessageOnStateChange() {
		for (ProjectState state : ProjectState.values())
			verifyStateText(state);
	}

	private void verifyStateText(ProjectState state) {
		statusBar.projectStateChanged(PROJECT_NAME, state);
		Label label = (Label) statusBar.getComponent(0);
		assertEquals(state.getMessage(PROJECT_NAME), label.getText());
	}
}