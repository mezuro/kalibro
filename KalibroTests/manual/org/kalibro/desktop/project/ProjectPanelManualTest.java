package org.kalibro.desktop.project;

import static org.kalibro.core.model.ProjectFixtures.*;

import org.kalibro.desktop.ComponentWrapperDialog;

public final class ProjectPanelManualTest extends ProjectPanel {

	public static void main(String[] args) {
		new ComponentWrapperDialog("ProjectPanel", new ProjectPanelManualTest()).setVisible(true);
		System.exit(0);
	}

	private ProjectPanelManualTest() {
		super();
		set(helloWorld());
	}
}