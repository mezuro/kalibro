package org.kalibro.core.persistence;

import static org.junit.Assert.*;
import static org.kalibro.RepositoryType.*;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.kalibro.Configuration;
import org.kalibro.MetricConfiguration;
import org.kalibro.Repository;
import org.kalibro.RepositoryType;
import org.kalibro.core.concurrent.VoidTask;
import org.kalibro.core.persistence.record.RepositoryRecord;
import org.kalibro.core.processing.ProcessTask;
import org.powermock.core.classloader.annotations.PrepareForTest;

@PrepareForTest({RepositoryDatabaseDao.class, RepositoryType.class})
public class RepositoryDatabaseDaoTest extends
	DatabaseDaoTestCase<Repository, RepositoryRecord, RepositoryDatabaseDao> {

	private static final Long ID = new Random().nextLong();
	private static final Long PROJECT_ID = new Random().nextLong();

	private Configuration configuration;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		configuration = loadFixture("sc", Configuration.class);
		when(entity.getConfiguration()).thenReturn(configuration);
	}

	@Test
	public void shouldGetSupportedRepositoryTypes() {
		RepositoryType supported = LOCAL_DIRECTORY;
		RepositoryType notSupported = mock(RepositoryType.class);
		mockStatic(RepositoryType.class);
		when(RepositoryType.values()).thenReturn(new RepositoryType[]{supported, notSupported});

		assertDeepEquals(set(supported), dao.supportedTypes());
	}

	@Test
	public void shouldGetRepositoriesOfProject() {
		assertDeepEquals(set(entity), dao.repositoriesOf(PROJECT_ID));

		verify(dao).createRecordQuery("repository.project = :projectId");
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

	@Test
	public void shouldNotProcessIfConfigurationHasNoNativeMetrics() throws Exception {
		for (MetricConfiguration metricConfiguration : configuration.getMetricConfigurations())
			if (!metricConfiguration.getMetric().isCompound())
				configuration.removeMetricConfiguration(metricConfiguration);
		mockProcessTask(0);
		assertThat(new VoidTask() {

			@Override
			protected void perform() {
				dao.process(ID);
			}
		}).throwsException().withMessage("Could not process repository '" + entity +
			"' because its configuration has no native metrics.");
		verifyNew(ProcessTask.class, never());
	}

	private ProcessTask mockProcessTask(Integer period) throws Exception {
		ProcessTask task = mock(ProcessTask.class);
		whenNew(ProcessTask.class).withArguments(entity).thenReturn(task);
		when(entity.getProcessPeriod()).thenReturn(period);
		return task;
	}
}