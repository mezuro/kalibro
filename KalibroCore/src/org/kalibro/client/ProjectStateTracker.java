package org.kalibro.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.kalibro.core.ProjectStateChangeSupport;
import org.kalibro.core.concurrent.Task;
import org.kalibro.core.model.enums.ProjectState;
import org.kalibro.core.persistence.dao.ProjectDao;

class ProjectStateTracker extends Task {

	private ProjectDao projectDao;
	private ProjectStateChangeSupport changeSupport;

	private Map<String, ProjectState> listenProjects;

	protected ProjectStateTracker(ProjectDao projectDao, ProjectStateChangeSupport changeSupport) {
		this.projectDao = projectDao;
		this.changeSupport = changeSupport;
		listenProjects = updatedListenProjects();
	}

	@Override
	public void perform() {
		Map<String, ProjectState> updatedListenProjects = updatedListenProjects();
		for (String projectName : updatedListenProjects.keySet()) {
			ProjectState updatedState = updatedListenProjects.get(projectName);
			if (updatedState != listenProjects.get(projectName))
				changeSupport.fireProjectStateChanged(projectName, updatedState);
		}
	}

	private Map<String, ProjectState> updatedListenProjects() {
		List<String> saved = projectDao.getProjectNames();
		Map<String, ProjectState> updatedListenProjects = new HashMap<String, ProjectState>();
		for (String projectName : changeSupport.getListenProjectNames())
			updatedListenProjects.put(projectName, updatedState(projectName, saved));
		return updatedListenProjects;
	}

	private ProjectState updatedState(String projectName, List<String> saved) {
		return saved.contains(projectName) ? projectDao.getProject(projectName).getState() : ProjectState.NEW;
	}
}