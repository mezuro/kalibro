package org.kalibro.dao;

import java.util.Date;

import org.kalibro.core.model.ProjectResult;

public interface ProjectResultDao {

	void save(ProjectResult projectResult);

	boolean hasResultsFor(String projectName);

	boolean hasResultsBefore(Date date, String projectName);

	boolean hasResultsAfter(Date date, String projectName);

	ProjectResult getFirstResultOf(String projectName);

	ProjectResult getLastResultOf(String projectName);

	ProjectResult getLastResultBefore(Date date, String projectName);

	ProjectResult getFirstResultAfter(Date date, String projectName);
}