package org.kalibro.dao;

import java.util.*;

import org.kalibro.core.model.Project;
import org.kalibro.core.model.enums.RepositoryType;

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

	@Override
	public Set<RepositoryType> getSupportedRepositoryTypes() {
		return null;
	}

	@Override
	public void processProject(String projectName) {
		return;
	}

	@Override
	public void processPeriodically(String projectName, Integer periodInDays) {
		return;
	}

	@Override
	public Integer getProcessPeriod(String projectName) {
		return null;
	}

	@Override
	public void cancelPeriodicProcess(String projectName) {
		return;
	}
}