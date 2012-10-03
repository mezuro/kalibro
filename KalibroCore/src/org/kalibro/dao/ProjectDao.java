package org.kalibro.dao;

import java.util.List;

import org.kalibro.Project;

public interface ProjectDao {

	void save(Project project);

	List<String> getProjectNames();

	boolean hasProject(String projectName);

	Project getProject(String projectName);

	void removeProject(String projectName);

	void processProject(String projectName);

	void processPeriodically(String projectName, Integer periodInDays);

	Integer getProcessPeriod(String projectName);

	void cancelPeriodicProcess(String projectName);
}