package org.kalibro.core.persistence.dao;

import java.util.Date;

import org.kalibro.core.model.ProjectResult;

public interface ProjectResultDao {

	public void save(ProjectResult projectResult);

	public boolean hasResultsFor(String projectName);

	public boolean hasResultsBefore(Date date, String projectName);

	public boolean hasResultsAfter(Date date, String projectName);

	public ProjectResult getFirstResultOf(String projectName);

	public ProjectResult getLastResultOf(String projectName);

	public ProjectResult getLastResultBefore(Date date, String projectName);

	public ProjectResult getFirstResultAfter(Date date, String projectName);
}