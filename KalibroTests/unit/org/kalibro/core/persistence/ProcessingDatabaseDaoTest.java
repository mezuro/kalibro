package org.kalibro.core.persistence;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.Random;

import javax.persistence.TypedQuery;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.ProcessState;
import org.kalibro.Processing;
import org.kalibro.core.persistence.record.ProcessingRecord;
import org.kalibro.tests.UnitTest;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ProcessingDatabaseDao.class)
public class ProcessingDatabaseDaoTest extends UnitTest {

	private static final String CLAUSE = "WHERE processing.repository.id = :repositoryId";

	private static final Long ID = Math.abs(new Random().nextLong());
	private static final Long TIME = Math.abs(new Random().nextLong());
	private static final Date DATE = new Date(TIME);

	private Processing processing;
	private ProcessingRecord record;

	private ProcessingDatabaseDao dao;

	@Before
	public void setUp() {
		processing = mock(Processing.class);
		record = mock(ProcessingRecord.class);
		when(record.convert()).thenReturn(processing);
		dao = spy(new ProcessingDatabaseDao(null));
	}

	@Test
	public void shouldSave() throws Exception {
		whenNew(ProcessingRecord.class).withArguments(processing).thenReturn(record);
		doReturn(record).when(dao).save(record);
		dao.save(processing);
		verify(dao).save(record);
	}

	@Test
	public void shouldAnswerIfHasProcessing() {
		doReturn(true).when(dao).exists(CLAUSE, "repositoryId", ID);
		assertTrue(dao.hasProcessing(ID));

		doReturn(false).when(dao).exists(CLAUSE, "repositoryId", ID);
		assertFalse(dao.hasProcessing(ID));
	}

	@Test
	public void shouldAnswerIfHasReadyProcessing() {
		doReturn(true).when(dao).exists(CLAUSE + " AND processing.state = 'READY'", "repositoryId", ID);
		assertTrue(dao.hasReadyProcessing(ID));

		doReturn(false).when(dao).exists(CLAUSE + " AND processing.state = 'READY'", "repositoryId", ID);
		assertFalse(dao.hasReadyProcessing(ID));
	}

	@Test
	public void shouldAnswerIfHasProcessingAfterDate() {
		doReturn(true).when(dao).exists(CLAUSE + " AND processing.date > :date", "repositoryId", ID, "date", TIME);
		assertTrue(dao.hasProcessingAfter(DATE, ID));

		doReturn(false).when(dao).exists(CLAUSE + " AND processing.date > :date", "repositoryId", ID, "date", TIME);
		assertFalse(dao.hasProcessingAfter(DATE, ID));
	}

	@Test
	public void shouldAnswerIfHasProcessingBeforeDate() {
		doReturn(true).when(dao).exists(CLAUSE + " AND processing.date < :date", "repositoryId", ID, "date", TIME);
		assertTrue(dao.hasProcessingBefore(DATE, ID));

		doReturn(false).when(dao).exists(CLAUSE + " AND processing.date < :date", "repositoryId", ID, "date", TIME);
		assertFalse(dao.hasProcessingBefore(DATE, ID));
	}

	@Test
	public void shouldGetLastProcessingState() {
		ProcessState state = ProcessState.ANALYZING;
		TypedQuery<String> query = PowerMockito.mock(TypedQuery.class);
		doReturn(query).when(dao).createQuery(
			"SELECT processing.state FROM Processing processing " + CLAUSE + " AND processing.date = " +
				"(SELECT max(p.date) FROM Processing p WHERE p.repository.id = :repositoryId)", String.class);
		when(query.getSingleResult()).thenReturn(state.name());

		assertSame(state, dao.lastProcessingState(ID));
		verify(query).setParameter("repositoryId", ID);
	}

	@Test
	public void shouldGetLastReadyProcessing() {
		TypedQuery<ProcessingRecord> query = prepareQuery(CLAUSE + " AND processing.date = " +
			"(SELECT max(p.date) FROM Processing p WHERE p.repository.id = :repositoryId AND p.state = 'READY')");
		assertSame(processing, dao.lastReadyProcessing(ID));
		verify(query).setParameter("repositoryId", ID);
	}

	@Test
	public void shouldGetFirstProcessing() {
		TypedQuery<ProcessingRecord> query = prepareQuery(CLAUSE + " AND processing.date = " +
			"(SELECT min(p.date) FROM Processing p WHERE p.repository.id = :repositoryId)");
		assertSame(processing, dao.firstProcessing(ID));
		verify(query).setParameter("repositoryId", ID);
	}

	@Test
	public void shouldGetLastProcessing() {
		TypedQuery<ProcessingRecord> query = prepareQuery(CLAUSE + " AND processing.date = " +
			"(SELECT max(p.date) FROM Processing p WHERE p.repository.id = :repositoryId)");
		assertSame(processing, dao.lastProcessing(ID));
		verify(query).setParameter("repositoryId", ID);
	}

	@Test
	public void shouldGetFirstProcessingAfterDate() {
		TypedQuery<ProcessingRecord> query = prepareQuery(CLAUSE + " AND processing.date = " +
			"(SELECT min(p.date) FROM Processing p WHERE p.repository.id = :repositoryId AND p.date > :date)");
		assertSame(processing, dao.firstProcessingAfter(DATE, ID));
		verify(query).setParameter("repositoryId", ID);
		verify(query).setParameter("date", TIME);
	}

	@Test
	public void shouldGetLastProcessingBeforeDate() {
		TypedQuery<ProcessingRecord> query = prepareQuery(CLAUSE + " AND processing.date = " +
			"(SELECT max(p.date) FROM Processing p WHERE p.repository.id = :repositoryId AND p.date < :date)");
		assertSame(processing, dao.lastProcessingBefore(DATE, ID));
		verify(query).setParameter("repositoryId", ID);
		verify(query).setParameter("date", TIME);
	}

	private TypedQuery<ProcessingRecord> prepareQuery(String clauses) {
		TypedQuery<ProcessingRecord> query = PowerMockito.mock(TypedQuery.class);
		doReturn(query).when(dao).createRecordQuery(clauses);
		when(query.getSingleResult()).thenReturn(record);
		return query;
	}
}