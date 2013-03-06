package org.kalibro.core.processing;

import static org.junit.Assert.*;
import static org.kalibro.Granularity.*;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.*;
import org.kalibro.core.concurrent.Producer;
import org.kalibro.core.concurrent.Writer;
import org.kalibro.core.persistence.ConfigurationDatabaseDao;
import org.kalibro.core.persistence.DatabaseDaoFactory;
import org.kalibro.core.persistence.MetricResultDatabaseDao;
import org.kalibro.core.persistence.ModuleResultDatabaseDao;
import org.kalibro.tests.UnitTest;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(AnalyzingTask.class)
public class AnalyzingTaskTest extends UnitTest {

	private static final Long PROCESSING_ID = new Random().nextLong();
	private static final String REPOSITORY_NAME = "AnalyzingTaskTest repository name";

	private Processing processing;
	private Configuration configuration;
	private SourceTreeBuilder treeBuilder;
	private CompoundResultCalculator configurer;
	private MetricResultDatabaseDao metricResultDao;
	private ModuleResultDatabaseDao moduleResultDao;

	private Module softwareModule, classModule;
	private ModuleResult softwareResult, classResult;

	private AnalyzingTask analyzingTask;

	@Before
	public void setUp() {
		analyzingTask = spy(new AnalyzingTask());
		mockDaos();
		mockEntities();
		stubModuleResultPreparation();
		stubResultProducer();
		mockStatic(CompoundResultCalculator.class);
	}

	private void mockDaos() {
		metricResultDao = mock(MetricResultDatabaseDao.class);
		moduleResultDao = mock(ModuleResultDatabaseDao.class);
		configuration = loadFixture("sc", Configuration.class);
		treeBuilder = mock(SourceTreeBuilder.class);
		DatabaseDaoFactory daoFactory = mock(DatabaseDaoFactory.class);
		ConfigurationDatabaseDao configurationDao = mock(ConfigurationDatabaseDao.class);
		doReturn(daoFactory).when(analyzingTask).daoFactory();
		when(daoFactory.createConfigurationDao()).thenReturn(configurationDao);
		when(daoFactory.createModuleResultDao()).thenReturn(moduleResultDao);
		when(configurationDao.snapshotFor(PROCESSING_ID)).thenReturn(configuration);
	}

	private void mockEntities() {
		processing = mock(Processing.class);
		Repository repository = mock(Repository.class);
		doReturn(processing).when(analyzingTask).processing();
		doReturn(repository).when(analyzingTask).repository();
		when(processing.getId()).thenReturn(PROCESSING_ID);
		when(repository.getName()).thenReturn(REPOSITORY_NAME);
	}

	private void stubModuleResultPreparation() {
		softwareModule = new Module(SOFTWARE, "null");
		softwareResult = new ModuleResult(null, softwareModule);
		when(moduleResultDao.prepareResultFor(softwareModule, PROCESSING_ID)).thenReturn(softwareResult);
		when(moduleResultDao.save(softwareResult, PROCESSING_ID)).thenReturn(softwareResult);

		classModule = new Module(CLASS, "HelloWorld");
		classResult = new ModuleResult(softwareResult, classModule);
		when(moduleResultDao.prepareResultFor(classModule, PROCESSING_ID)).thenReturn(classResult);
		when(moduleResultDao.save(classResult, PROCESSING_ID)).thenReturn(classResult);
	}

	private void stubResultProducer() {
		Producer<NativeModuleResult> resultProducer = new Producer<NativeModuleResult>();
		Writer<NativeModuleResult> writer = resultProducer.createWriter();
		writer.write(newResult(classModule, "cbo", 0.0, "lcom4", 1.0));
		writer.write(newResult(softwareModule, "total_cof", 1.0));
		writer.close();
		doReturn(resultProducer).when(analyzingTask).resultProducer();
	}

	private NativeModuleResult newResult(Module module, Object... results) {
		NativeModuleResult moduleResult = new NativeModuleResult(module);
		for (int i = 0; i < results.length; i += 2) {
			NativeMetric metric = loadFixture(results[i].toString(), NativeMetric.class);
			Double value = (Double) results[i + 1];
			moduleResult.addMetricResult(new NativeMetricResult(metric, value));
		}
		return moduleResult;
	}

	@Test
	public void shouldAddMetricResults() {
		NativeMetric lcom4 = loadFixture("lcom4", NativeMetric.class);
		assertFalse(classResult.hasResultFor(lcom4));
		analyzingTask.perform();

		assertTrue(softwareResult.hasResultFor(lcom4));
		MetricResult lcom4Result = classResult.getResultFor(lcom4);
		assertDoubleEquals(1.0, lcom4Result.getValue());
		assertTrue(lcom4Result.getDescendantResults().isEmpty());
	}

	@Test
	public void shouldAddDescendantResults() {
		NativeMetric cbo = loadFixture("cbo", NativeMetric.class);
		assertFalse(softwareResult.hasResultFor(cbo));
		analyzingTask.perform();

		assertTrue(softwareResult.hasResultFor(cbo));
		MetricResult cboResult = softwareResult.getResultFor(cbo);
		assertDoubleEquals(Double.NaN, cboResult.getValue());
		assertDoubleEquals(0.0, cboResult.getAggregatedValue());
		assertDeepEquals(list(0.0), cboResult.getDescendantResults());
	}

	@Test
	public void shouldConfigureResults() {
		analyzingTask.perform();
		verifyStatic(times(2));
		CompoundResultCalculator.configure(softwareResult, configuration);
		verifyStatic();
		CompoundResultCalculator.configure(classResult, configuration);
	}

	@Test
	public void shouldSaveResults() {
		analyzingTask.perform();
		verify(moduleResultDao, times(3)).save(softwareResult, PROCESSING_ID);
		verify(moduleResultDao).save(classResult, PROCESSING_ID);
	}

	@Test
	public void shouldSetRootOnProcessing() {
		analyzingTask.perform();
		assertArrayEquals(array(REPOSITORY_NAME), softwareModule.getName());
		verify(processing, times(2)).setResultsRoot(softwareResult);
	}
}