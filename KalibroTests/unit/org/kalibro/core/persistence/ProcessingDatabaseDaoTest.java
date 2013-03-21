package org.kalibro.core.persistence;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.Random;

import org.junit.Test;
import org.kalibro.*;
import org.kalibro.core.persistence.record.MetricConfigurationSnapshotRecord;
import org.kalibro.core.persistence.record.ProcessingRecord;
import org.powermock.core.classloader.annotations.PrepareForTest;

@PrepareForTest(ProcessingDatabaseDao.class)
public class ProcessingDatabaseDaoTest extends
	DatabaseDaoTestCase<Processing, ProcessingRecord, ProcessingDatabaseDao> {

	private static final String WHERE = "processing.repository = :repositoryId";
	private static final String CLAUSE = "WHERE " + WHERE;

	private static final Long ID = Math.abs(new Random().nextLong());
	private static final Long TIME = Math.abs(new Random().nextLong());
	private static final Date DATE = new Date(TIME);

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
		when(entity.getState()).thenReturn(ProcessState.COLLECTING);
		assertEquals(ProcessState.COLLECTING, dao.lastProcessingState(ID));

		verify(dao).createRecordQuery(WHERE + " AND processing.date = " +
			"(SELECT max(p.date) FROM Processing p WHERE p.repository = :repositoryId)");
		verify(query).setParameter("repositoryId", ID);
	}

	@Test
	public void shouldGetLastReadyProcessing() {
		assertSame(entity, dao.lastReadyProcessing(ID));

		verify(dao).createRecordQuery(WHERE + " AND processing.date = " +
			"(SELECT max(p.date) FROM Processing p WHERE p.repository = :repositoryId AND p.state = 'READY')");
		verify(query).setParameter("repositoryId", ID);
	}

	@Test
	public void shouldGetFirstProcessing() {
		assertSame(entity, dao.firstProcessing(ID));

		verify(dao).createRecordQuery(WHERE + " AND processing.date = " +
			"(SELECT min(p.date) FROM Processing p WHERE p.repository = :repositoryId)");
		verify(query).setParameter("repositoryId", ID);
	}

	@Test
	public void shouldGetLastProcessing() {
		assertSame(entity, dao.lastProcessing(ID));

		verify(dao).createRecordQuery(WHERE + " AND processing.date = " +
			"(SELECT max(p.date) FROM Processing p WHERE p.repository = :repositoryId)");
		verify(query).setParameter("repositoryId", ID);
	}

	@Test
	public void shouldGetFirstProcessingAfterDate() {
		assertSame(entity, dao.firstProcessingAfter(DATE, ID));

		verify(dao).createRecordQuery(WHERE + " AND processing.date = " +
			"(SELECT min(p.date) FROM Processing p WHERE p.repository = :repositoryId AND p.date > :date)");
		verify(query).setParameter("repositoryId", ID);
		verify(query).setParameter("date", TIME);
	}

	@Test
	public void shouldGetLastProcessingBeforeDate() {
		assertSame(entity, dao.lastProcessingBefore(DATE, ID));

		verify(dao).createRecordQuery(WHERE + " AND processing.date = " +
			"(SELECT max(p.date) FROM Processing p WHERE p.repository = :repositoryId AND p.date < :date)");
		verify(query).setParameter("repositoryId", ID);
		verify(query).setParameter("date", TIME);
	}

	@Test
	public void shouldCreateProcessingForRepository() throws Exception {
		Repository repository = mock(Repository.class);
		MetricConfigurationSnapshotRecord snapshot = mockSnapshot(repository);
		whenNew(Processing.class).withNoArguments().thenReturn(entity);

		assertSame(entity, dao.createProcessingFor(repository));
		verify(dao).save(record);
		verify(dao).saveAll(list(snapshot));
	}

	private MetricConfigurationSnapshotRecord mockSnapshot(Repository repository) throws Exception {
		Configuration configuration = mock(Configuration.class);
		MetricConfiguration metricConf = mock(MetricConfiguration.class);
		MetricConfigurationSnapshotRecord snapshot = mock(MetricConfigurationSnapshotRecord.class);
		when(repository.getConfiguration()).thenReturn(configuration);
		doReturn(sortedSet(metricConf)).when(configuration).getMetricConfigurations();
		whenNew(MetricConfigurationSnapshotRecord.class).withArguments(metricConf, record.id()).thenReturn(snapshot);
		doNothing().when(dao).saveAll(list(snapshot));
		return snapshot;
	}

	@Test
	public void shouldSaveProcessing() throws Exception {
		dao.save(entity, ID);

		verifyNew(ProcessingRecord.class).withArguments(entity, ID);
		verify(dao).save(record);
	}
}