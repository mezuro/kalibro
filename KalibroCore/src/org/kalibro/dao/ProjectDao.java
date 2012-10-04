package org.kalibro.dao;

import java.util.List;
import java.util.SortedSet;

import org.kalibro.Project;

public interface ProjectDao {

	Long save(Project project);

	List<String> getProjectNames();

	boolean hasProject(String projectName);

	Project getProject(String projectName);

	void removeProject(String projectName);

	void processProject(String projectName);

	void processPeriodically(String projectName, Integer periodInDays);

	Integer getProcessPeriod(String projectName);

	void cancelPeriodicProcess(String projectName);

	SortedSet<Project> all();

	void delete(Long projectId);
}