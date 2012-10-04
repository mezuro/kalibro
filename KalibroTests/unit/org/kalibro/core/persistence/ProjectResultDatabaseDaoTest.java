package org.kalibro.core.persistence;

import static org.junit.Assert.*;
import static org.kalibro.ProjectResultFixtures.helloWorldResult;

import java.util.Date;

import javax.persistence.TypedQuery;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.RepositoryResult;
import org.kalibro.core.persistence.record.ProjectResultRecord;
import org.kalibro.tests.UnitTest;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

public class ProjectResultDatabaseDaoTest extends UnitTest {

	private static final String COUNT_QUERY = "SELECT count(result) FROM RepositoryResult result " +
		"WHERE result.project.name = :projectName";
	private static final String LAST_QUERY = "SELECT r FROM RepositoryResult r " +
		"WHERE r.project.name = :projectName AND r.date = " +
		"(SELECT max(result.date) FROM RepositoryResult result WHERE result.project.name = :projectName AND ";
	private static final String FIRST_QUERY = LAST_QUERY.replace("SELECT max", "SELECT min");

	private RecordManager recordManager;
	private RepositoryResult repositoryResult;
	private String projectName;
	private Date date;

	private ProjectResultDatabaseDao dao;

	@Before
	public void setUp() {
		repositoryResult = helloWorldResult();
		projectName = repositoryResult.getProject().getName();
		date = repositoryResult.getDate();
		recordManager = PowerMockito.mock(RecordManager.class);
		dao = PowerMockito.spy(new ProjectResultDatabaseDao(recordManager));
	}

	@Test
	public void testSave() {
		dao.save(repositoryResult);

		ArgumentCaptor<ProjectResultRecord> captor = ArgumentCaptor.forClass(ProjectResultRecord.class);
		Mockito.verify(recordManager).save(captor.capture());
		assertExpected(captor.getValue().convert());
	}

	@Test
	public void testHasResults() {
		String queryText = COUNT_QUERY + " AND 1 = 1";

		TypedQuery<Long> query = prepareCountQuery(queryText, 0);
		assertFalse(dao.hasResultsFor(projectName));
		Mockito.verify(query).setParameter("projectName", projectName);

		query = prepareCountQuery(queryText, 1);
		assertTrue(dao.hasResultsFor(projectName));
		Mockito.verify(query).setParameter("projectName", projectName);
	}

	@Test
	public void testHasResultsBefore() {
		String queryText = COUNT_QUERY + " AND result.date < :date";

		TypedQuery<Long> query = prepareCountQuery(queryText, 0);
		assertFalse(dao.hasResultsBefore(date, projectName));
		Mockito.verify(query).setParameter("projectName", projectName);
		Mockito.verify(query).setParameter("date", date.getTime());

		query = prepareCountQuery(queryText, 1);
		assertTrue(dao.hasResultsBefore(date, projectName));
		Mockito.verify(query).setParameter("projectName", projectName);
		Mockito.verify(query).setParameter("date", date.getTime());
	}

	@Test
	public void testHasResultsAfter() {
		String queryText = COUNT_QUERY + " AND result.date > :date";

		TypedQuery<Long> query = prepareCountQuery(queryText, 0);
		assertFalse(dao.hasResultsAfter(date, projectName));
		Mockito.verify(query).setParameter("projectName", projectName);
		Mockito.verify(query).setParameter("date", date.getTime());

		query = prepareCountQuery(queryText, 1);
		assertTrue(dao.hasResultsAfter(date, projectName));
		Mockito.verify(query).setParameter("projectName", projectName);
		Mockito.verify(query).setParameter("date", date.getTime());
	}

	private TypedQuery<Long> prepareCountQuery(String queryText, long count) {
		TypedQuery<Long> query = PowerMockito.mock(TypedQuery.class);
		PowerMockito.when(recordManager.createQuery(queryText, Long.class)).thenReturn(query);
		PowerMockito.when(query.getSingleResult()).thenReturn(count);
		return query;
	}

	@Test
	public void testFirstResult() {
		TypedQuery<ProjectResultRecord> query = prepareResultQuery(FIRST_QUERY + "1 = 1)");
		assertExpected(dao.getFirstResultOf(projectName));
		Mockito.verify(query).setParameter("projectName", projectName);
	}

	@Test
	public void testLastResult() {
		TypedQuery<ProjectResultRecord> query = prepareResultQuery(LAST_QUERY + "1 = 1)");
		assertExpected(dao.getLastResultOf(projectName));
		Mockito.verify(query).setParameter("projectName", projectName);
	}

	@Test
	public void testFirstResultAfter() {
		TypedQuery<ProjectResultRecord> query = prepareResultQuery(FIRST_QUERY + "result.date > :date)");
		assertExpected(dao.getFirstResultAfter(date, projectName));
		Mockito.verify(query).setParameter("projectName", projectName);
		Mockito.verify(query).setParameter("date", date.getTime());
	}

	@Test
	public void testLastResultBefore() {
		TypedQuery<ProjectResultRecord> query = prepareResultQuery(LAST_QUERY + "result.date < :date)");
		assertExpected(dao.getLastResultBefore(date, projectName));
		Mockito.verify(query).setParameter("projectName", projectName);
		Mockito.verify(query).setParameter("date", date.getTime());
	}

	private void assertExpected(RepositoryResult actual) {
		actual.setSourceTree(repositoryResult.getSourceTree());
		actual.getProject().setConfigurationName(repositoryResult.getProject().getConfigurationName());
		assertDeepEquals(repositoryResult, actual);
	}

	private TypedQuery<ProjectResultRecord> prepareResultQuery(String queryText) {
		TypedQuery<ProjectResultRecord> query = PowerMockito.mock(TypedQuery.class);
		ProjectResultRecord record = new ProjectResultRecord(repositoryResult);
		PowerMockito.doReturn(query).when(dao).createRecordQuery(queryText);
		PowerMockito.when(query.getSingleResult()).thenReturn(record);
		return query;
	}
}