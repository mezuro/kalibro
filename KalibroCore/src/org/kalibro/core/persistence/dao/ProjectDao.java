package org.kalibro.core.persistence.dao;

import java.util.List;

import org.kalibro.core.model.Project;

public interface ProjectDao {

	public void save(Project project);

	public List<String> getProjectNames();

	public Project getProject(String projectName);

	public void removeProject(String projectName);
}