package org.kalibro.dao;

import java.util.Date;

import org.kalibro.Processing;

public interface ProcessingDao {

	void save(Processing repositoryResult);

	boolean hasResultsFor(String projectName);

	boolean hasResultsBefore(Date date, String projectName);

	boolean hasResultsAfter(Date date, String projectName);

	Processing getFirstResultOf(String projectName);

	Processing getLastResultOf(String projectName);

	Processing getLastResultBefore(Date date, String projectName);

	Processing getFirstResultAfter(Date date, String projectName);
}