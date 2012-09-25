package org.kalibro.desktop.project;

import static org.kalibro.RepositoryType.*;
import static org.kalibro.core.model.RepositoryFixtures.*;

import org.kalibro.desktop.ComponentWrapperDialog;

public final class RepositoryPanelManualTest extends RepositoryPanel {

	public static void main(String[] args) {
		new ComponentWrapperDialog("RepositoryPanel", new RepositoryPanelManualTest()).setVisible(true);
		System.exit(0);
	}

	private RepositoryPanelManualTest() {
		super();
		set(helloWorldRepository(GIT));
	}
}