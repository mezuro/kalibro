package org.kalibro.desktop.project;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;

import org.kalibro.core.model.Project;
import org.kalibro.desktop.CrudMenu;

public class ProjectMenu extends CrudMenu<Project> {

	public ProjectMenu(JDesktopPane desktopPane) {
		super(desktopPane, "Project");
	}

	@Override
	protected void initializeController() {
		controller = new ProjectController(desktopPane);
	}

	@Override
	protected boolean isEntityFrame(JInternalFrame frame) {
		return frame instanceof ProjectFrame;
	}
}