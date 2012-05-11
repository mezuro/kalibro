package org.kalibro.core.persistence.dao;

import java.util.Date;
import java.util.SortedMap;
import java.util.TreeMap;

import org.kalibro.core.model.ProjectResult;

public class ProjectResultDaoFake implements ProjectResultDao {

	private static final Date BEGIN = new Date(0);
	private static final Date END = new Date(Long.MAX_VALUE);

	private SortedMap<ProjectResultKey, ProjectResult> projectResults = new TreeMap<ProjectResultKey, ProjectResult>();

	@Override
	public void save(ProjectResult projectResult) {
		projectResults.put(new ProjectResultKey(projectResult), projectResult);
	}

	@Override
	public boolean hasResultsFor(String projectName) {
		return hasResultsBetween(projectName, BEGIN, END);
	}

	@Override
	public boolean hasResultsBefore(Date date, String projectName) {
		return hasResultsBetween(projectName, BEGIN, date);
	}

	@Override
	public boolean hasResultsAfter(Date date, String projectName) {
		return hasResultsBetween(projectName, new Date(date.getTime() + 1), END);
	}

	private boolean hasResultsBetween(String projectName, Date from, Date to) {
		return !subMap(projectName, from, to).isEmpty();
	}

	@Override
	public ProjectResult getFirstResultOf(String projectName) {
		ProjectResultKey key = subMap(projectName, BEGIN, END).firstKey();
		return projectResults.get(key);
	}

	@Override
	public ProjectResult getLastResultOf(String projectName) {
		ProjectResultKey key = subMap(projectName, BEGIN, END).lastKey();
		return projectResults.get(key);
	}

	@Override
	public ProjectResult getLastResultBefore(Date date, String projectName) {
		ProjectResultKey key = subMap(projectName, BEGIN, date).lastKey();
		return projectResults.get(key);
	}

	@Override
	public ProjectResult getFirstResultAfter(Date date, String projectName) {
		ProjectResultKey key = subMap(projectName, new Date(date.getTime() + 1), END).firstKey();
		return projectResults.get(key);
	}

	private SortedMap<ProjectResultKey, ProjectResult> subMap(String projectName, Date from, Date to) {
		ProjectResultKey fromKey = new ProjectResultKey(projectName, from);
		ProjectResultKey toKey = new ProjectResultKey(projectName, to);
		return projectResults.subMap(fromKey, toKey);
	}
}