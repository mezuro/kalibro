package org.kalibro.desktop.project;

import org.kalibro.Project;
import org.kalibro.desktop.swingextension.InternalFrame;

public class ProjectFrame extends InternalFrame<Project> {

	private ProjectPanel projectPanel;

	public ProjectFrame(Project project) {
		super("project", project.getName() + " - Project", project);
	}

	@Override
	protected ProjectPanel buildContentPane() {
		projectPanel = new ProjectPanel();
		projectPanel.set(entity);
		return projectPanel;
	}

	@Override
	public Project get() {
		entity = projectPanel.get();
		return entity;
	}
}