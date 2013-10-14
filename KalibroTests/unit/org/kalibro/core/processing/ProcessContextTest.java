package org.kalibro.core.processing;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.Configuration;
import org.kalibro.NativeModuleResult;
import org.kalibro.Processing;
import org.kalibro.Repository;
import org.kalibro.core.concurrent.Producer;
import org.kalibro.core.persistence.MetricResultDatabaseDao;
import org.kalibro.core.persistence.ModuleResultDatabaseDao;
import org.kalibro.core.persistence.ProcessingDatabaseDao;
import org.kalibro.tests.UnitTest;

public class ProcessContextTest extends UnitTest {

	private Repository repository;

	private ProcessContext context;

	@Before
	public void setUp() {
		repository = mock(Repository.class);
		context = new ProcessContext(repository);
	}

	@Test
	public void shouldRetrieveRepository() {
		assertSame(repository, context.repository());
	}

	@Test
	public void shouldRetrieveProcessing() {
		Processing processing = mock(Processing.class);
		context.setProcessing(processing);
		assertSame(processing, context.processing());
	}

	@Test
	public void shouldRetrieveConfigurationSnapshot() {
		Configuration configuration = mock(Configuration.class);
		context.setConfigurationSnapshot(configuration);
		assertSame(configuration, context.configuration());
	}

	@Test
	public void shouldRetrieveCodeDirectory() {
		File codeDirectory = mock(File.class);
		context.setCodeDirectory(codeDirectory);
		assertSame(codeDirectory, context.codeDirectory());
	}

	@Test
	public void shouldRetrieveResultProducer() {
		Producer<NativeModuleResult> producer = mock(Producer.class);
		context.setProducer(producer);
		assertSame(producer, context.resultProducer());
	}

	@Test
	public void shouldRetrieveProcessingDao() {
		ProcessingDatabaseDao processingDao = mock(ProcessingDatabaseDao.class);
		context.setProcessingDao(processingDao);
		assertSame(processingDao, context.processingDao());
	}

	@Test
	public void shouldRetrieveModuleResultDao() {
		ModuleResultDatabaseDao moduleResultDao = mock(ModuleResultDatabaseDao.class);
		context.setModuleResultDao(moduleResultDao);
		assertSame(moduleResultDao, context.moduleResultDao());
	}

	@Test
	public void shouldRetrieveMetricResultDao() {
		MetricResultDatabaseDao metricResultDao = mock(MetricResultDatabaseDao.class);
		context.setMetricResultDao(metricResultDao);
		assertSame(metricResultDao, context.metricResultDao());
	}
}