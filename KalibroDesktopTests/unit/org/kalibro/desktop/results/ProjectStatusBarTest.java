package org.kalibro.desktop.results;

import static org.junit.Assert.assertEquals;
import static org.kalibro.core.model.ProjectFixtures.*;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.Project;
import org.kalibro.ProjectState;
import org.kalibro.desktop.swingextension.Label;
import org.kalibro.tests.UnitTest;

public class ProjectStatusBarTest extends UnitTest {

	private Project project;
	private ProjectStatusBar statusBar;

	@Before
	public void setUp() {
		project = helloWorld();
		statusBar = new ProjectStatusBar(project);
	}

	@Test
	public void shouldChangeMessageOnStateChange() {
		for (ProjectState state : ProjectState.values())
			verifyStateText(state);
	}

	private void verifyStateText(ProjectState state) {
		statusBar.setProjectState(state);
		Label label = (Label) statusBar.getComponent(0);
		assertEquals(state.getMessage(PROJECT_NAME), label.getText());
	}
}