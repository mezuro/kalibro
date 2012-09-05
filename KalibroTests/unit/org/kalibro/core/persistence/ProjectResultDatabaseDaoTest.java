package org.kalibro.core.persistence;

import static org.junit.Assert.*;
import static org.kalibro.core.model.ProjectResultFixtures.helloWorldResult;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.TestCase;
import org.kalibro.core.model.ProjectResult;
import org.kalibro.core.persistence.record.ProjectResultRecord;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

public class ProjectResultDatabaseDaoTest extends TestCase {

	private static final String COUNT_QUERY = "SELECT count(result) FROM ProjectResult result " +
		"WHERE result.project.name = :projectName";
	private static final String LAST_QUERY = "SELECT r FROM ProjectResult r " +
		"WHERE r.project.name = :projectName AND r.date = " +
		"(SELECT max(result.date) FROM ProjectResult result WHERE result.project.name = :projectName AND ";
	private static final String FIRST_QUERY = LAST_QUERY.replace("SELECT max", "SELECT min");

	private DatabaseManager databaseManager;
	private ProjectResult projectResult;
	private String projectName;
	private Date date;

	private ProjectResultDatabaseDao dao;

	@Before
	public void setUp() {
		projectResult = helloWorldResult();
		projectName = projectResult.getProject().getName();
		date = projectResult.getDate();
		databaseManager = PowerMockito.mock(DatabaseManager.class);
		dao = PowerMockito.spy(new ProjectResultDatabaseDao(databaseManager));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testSave() {
		dao.save(projectResult);

		ArgumentCaptor<ProjectResultRecord> captor = ArgumentCaptor.forClass(ProjectResultRecord.class);
		Mockito.verify(databaseManager).save(captor.capture());
		assertExpected(captor.getValue().convert());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testHasResults() {
		String queryText = COUNT_QUERY + " AND 1 = 1";

		Query<Long> query = prepareCountQuery(queryText, 0);
		assertFalse(dao.hasResultsFor(projectName));
		Mockito.verify(query).setParameter("projectName", projectName);

		query = prepareCountQuery(queryText, 1);
		assertTrue(dao.hasResultsFor(projectName));
		Mockito.verify(query).setParameter("projectName", projectName);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testHasResultsBefore() {
		String queryText = COUNT_QUERY + " AND result.date < :date";

		Query<Long> query = prepareCountQuery(queryText, 0);
		assertFalse(dao.hasResultsBefore(date, projectName));
		Mockito.verify(query).setParameter("projectName", projectName);
		Mockito.verify(query).setParameter("date", date.getTime());

		query = prepareCountQuery(queryText, 1);
		assertTrue(dao.hasResultsBefore(date, projectName));
		Mockito.verify(query).setParameter("projectName", projectName);
		Mockito.verify(query).setParameter("date", date.getTime());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testHasResultsAfter() {
		String queryText = COUNT_QUERY + " AND result.date > :date";

		Query<Long> query = prepareCountQuery(queryText, 0);
		assertFalse(dao.hasResultsAfter(date, projectName));
		Mockito.verify(query).setParameter("projectName", projectName);
		Mockito.verify(query).setParameter("date", date.getTime());

		query = prepareCountQuery(queryText, 1);
		assertTrue(dao.hasResultsAfter(date, projectName));
		Mockito.verify(query).setParameter("projectName", projectName);
		Mockito.verify(query).setParameter("date", date.getTime());
	}

	private Query<Long> prepareCountQuery(String queryText, long count) {
		Query<Long> query = PowerMockito.mock(Query.class);
		PowerMockito.when(databaseManager.createQuery(queryText, Long.class)).thenReturn(query);
		PowerMockito.when(query.getSingleResult()).thenReturn(count);
		return query;
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testFirstResult() {
		Query<ProjectResultRecord> query = prepareResultQuery(FIRST_QUERY + "1 = 1)");
		assertExpected(dao.getFirstResultOf(projectName));
		Mockito.verify(query).setParameter("projectName", projectName);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testLastResult() {
		Query<ProjectResultRecord> query = prepareResultQuery(LAST_QUERY + "1 = 1)");
		assertExpected(dao.getLastResultOf(projectName));
		Mockito.verify(query).setParameter("projectName", projectName);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testFirstResultAfter() {
		Query<ProjectResultRecord> query = prepareResultQuery(FIRST_QUERY + "result.date > :date)");
		assertExpected(dao.getFirstResultAfter(date, projectName));
		Mockito.verify(query).setParameter("projectName", projectName);
		Mockito.verify(query).setParameter("date", date.getTime());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testLastResultBefore() {
		Query<ProjectResultRecord> query = prepareResultQuery(LAST_QUERY + "result.date < :date)");
		assertExpected(dao.getLastResultBefore(date, projectName));
		Mockito.verify(query).setParameter("projectName", projectName);
		Mockito.verify(query).setParameter("date", date.getTime());
	}

	private void assertExpected(ProjectResult actual) {
		actual.setSourceTree(projectResult.getSourceTree());
		actual.getProject().setConfigurationName(projectResult.getProject().getConfigurationName());
		assertDeepEquals(projectResult, actual);
	}

	private Query<ProjectResultRecord> prepareResultQuery(String queryText) {
		Query<ProjectResultRecord> query = PowerMockito.mock(Query.class);
		ProjectResultRecord record = new ProjectResultRecord(projectResult);
		PowerMockito.doReturn(query).when(dao).createRecordQuery(queryText);
		PowerMockito.when(query.getSingleResult("No project result found")).thenReturn(record);
		return query;
	}
}