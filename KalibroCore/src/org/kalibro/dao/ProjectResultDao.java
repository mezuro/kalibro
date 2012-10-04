package org.kalibro.dao;

import java.util.Date;

import org.kalibro.RepositoryResult;

public interface ProjectResultDao {

	void save(RepositoryResult repositoryResult);

	boolean hasResultsFor(String projectName);

	boolean hasResultsBefore(Date date, String projectName);

	boolean hasResultsAfter(Date date, String projectName);

	RepositoryResult getFirstResultOf(String projectName);

	RepositoryResult getLastResultOf(String projectName);

	RepositoryResult getLastResultBefore(Date date, String projectName);

	RepositoryResult getFirstResultAfter(Date date, String projectName);
}