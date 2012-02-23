package org.kalibro.core.persistence.dao;

import java.util.List;

import org.kalibro.core.model.Project;

public interface ProjectDao {

	void save(Project project);

	List<String> getProjectNames();

	Project getProject(String projectName);

	void removeProject(String projectName);
}