package org.kalibro.desktop.project;

import java.util.List;

import javax.swing.JDesktopPane;

import org.kalibro.core.model.Project;
import org.kalibro.core.persistence.dao.DaoFactory;
import org.kalibro.core.persistence.dao.ProjectDao;
import org.kalibro.desktop.CrudController;

public class ProjectController extends CrudController<Project> {

	public ProjectController(JDesktopPane desktopPane) {
		super(desktopPane, "Project");
	}

	@Override
	protected Project createEntity(String name) {
		Project project = new Project();
		project.setName(name);
		return project;
	}

	@Override
	protected List<String> getEntityNames() {
		return projectDao().getProjectNames();
	}

	@Override
	protected Project getEntity(String name) {
		return projectDao().getProject(name);
	}

	@Override
	protected ProjectFrame createFrameFor(Project project) {
		return new ProjectFrame(project);
	}

	@Override
	protected void removeEntity(String name) {
		projectDao().removeProject(name);
	}

	@Override
	protected void save(Project project) {
		projectDao().save(project);
	}

	@Override
	protected void setEntityName(Project project, String name) {
		project.setName(name);
	}

	private ProjectDao projectDao() {
		return DaoFactory.getProjectDao();
	}
}