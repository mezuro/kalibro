package org.kalibro.core.persistence;

import static org.junit.Assert.*;
import static org.kalibro.RepositoryType.LOCAL_DIRECTORY;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.kalibro.Repository;
import org.kalibro.RepositoryType;
import org.kalibro.core.persistence.record.RepositoryRecord;
import org.kalibro.core.processing.ProcessTask;
import org.powermock.core.classloader.annotations.PrepareForTest;

@PrepareForTest({RepositoryDatabaseDao.class, RepositoryType.class})
public class RepositoryDatabaseDaoTest extends
	DatabaseDaoTestCase<Repository, RepositoryRecord, RepositoryDatabaseDao> {

	private static final Long ID = new Random().nextLong();
	private static final Long PROJECT_ID = new Random().nextLong();

	@Test
	public void shouldGetSupportedRepositoryTypes() {
		RepositoryType supported = LOCAL_DIRECTORY;
		RepositoryType notSupported = mock(RepositoryType.class);
		mockStatic(RepositoryType.class);
		when(RepositoryType.values()).thenReturn(new RepositoryType[]{supported, notSupported});

		assertDeepEquals(set(supported), dao.supportedTypes());
	}

	@Test
	public void shouldGetRepositoryOfProcessing() {
		assertSame(entity, dao.repositoryOf(ID));

		String from = "Processing processing JOIN processing.repository repository";
		verify(dao).createRecordQuery(from, "processing.id = :processingId");
		verify(query).setParameter("processingId", ID);
	}

	@Test
	public void shouldGetRepositoriesOfProject() {
		assertDeepEquals(set(entity), dao.repositoriesOf(PROJECT_ID));

		verify(dao).createRecordQuery("repository.project.id = :projectId");
		verify(query).setParameter("projectId", PROJECT_ID);
	}

	@Test
	public void shouldSave() throws Exception {
		when(record.id()).thenReturn(ID);
		assertEquals(ID, dao.save(entity, PROJECT_ID));

		verifyNew(RepositoryRecord.class).withArguments(entity, PROJECT_ID);
		verify(dao).save(record);
	}

	@Test
	public void shouldProcessOnce() throws Exception {
		ProcessTask task = mockProcessTask(0);
		dao.process(ID);
		verify(task).executeInBackground();
	}

	@Test
	public void shouldProcessPeriodically() throws Exception {
		ProcessTask task = mockProcessTask(42);
		dao.process(ID);
		verify(task).executePeriodically(42, TimeUnit.DAYS);
	}

	@Test
	public void shouldCancelProcessing() throws Exception {
		ProcessTask task = mockProcessTask(0);
		dao.process(ID);
		dao.cancelProcessing(ID);
		verify(task).cancelExecution();
	}

	private ProcessTask mockProcessTask(Integer period) throws Exception {
		ProcessTask task = mock(ProcessTask.class);
		whenNew(ProcessTask.class).withArguments(entity).thenReturn(task);
		when(entity.getProcessPeriod()).thenReturn(period);
		return task;
	}
}