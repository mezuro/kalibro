package org.kalibro.core.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.kalibro.core.model.Project;

public class ProjectDaoFake implements ProjectDao {

	private Map<String, Project> projects = new TreeMap<String, Project>();

	@Override
	public void save(Project project) {
		projects.put(project.getName(), project);
	}

	@Override
	public List<String> getProjectNames() {
		return new ArrayList<String>(projects.keySet());
	}

	@Override
	public boolean hasProject(String projectName) {
		return projects.containsKey(projectName);
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