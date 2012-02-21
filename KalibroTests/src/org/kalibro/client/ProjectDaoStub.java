package org.kalibro.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.kalibro.core.model.Project;
import org.kalibro.core.persistence.dao.ProjectDao;

class ProjectDaoStub implements ProjectDao {

	private Map<String, Project> projects = new HashMap<String, Project>();

	@Override
	public void save(Project project) {
		projects.put(project.getName(), project);
	}

	@Override
	public List<String> getProjectNames() {
		return new ArrayList<String>(projects.keySet());
	}

	@Override
	public Project getProject(String projectName) {
		return projects.get(projectName);
	}

	@Override
	public void removeProject(String projectName) {
		projects.remove(projectName);
	}
}