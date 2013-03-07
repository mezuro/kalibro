package org.kalibro.core.processing;

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
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(AnalyzingTask.class)
public class AnalyzingTaskTest extends UnitTest {

	private static final Long ROOT_ID = new Random().nextLong();
	private static final Long CLASS_ID = new Random().nextLong();
	private static final Long PROCESSING_ID = new Random().nextLong();
	private static final String REPOSITORY_NAME = "AnalyzingTaskTest repository name";

	private Processing processing;
	private Configuration configuration;
	private SourceTreeBuilder treeBuilder;
	private ModuleResultConfigurer configurer;
	private MetricResultDatabaseDao metricResultDao;
	private ModuleResultDatabaseDao moduleResultDao;

	private Module softwareModule, classModule;
	private ModuleResult softwareResult, classResult;

	private AnalyzingTask analyzingTask;

	@Before
	public void setUp() throws Exception {
		analyzingTask = spy(new AnalyzingTask());
		mockDaos();
		mockEntities();
		mockSourceTreeBuilder();
		mockModuleResultConfigurer();
		stubResultProducer();
	}

	private void mockDaos() {
		metricResultDao = mock(MetricResultDatabaseDao.class);
		moduleResultDao = mock(ModuleResultDatabaseDao.class);
		configuration = loadFixture("sc", Configuration.class);
		treeBuilder = mock(SourceTreeBuilder.class);
		DatabaseDaoFactory daoFactory = mock(DatabaseDaoFactory.class);
		ConfigurationDatabaseDao configurationDao = mock(ConfigurationDatabaseDao.class);

		doReturn(daoFactory).when(analyzingTask).daoFactory();
		when(daoFactory.createMetricResultDao()).thenReturn(metricResultDao);
		when(daoFactory.createModuleResultDao()).thenReturn(moduleResultDao);
		when(daoFactory.createConfigurationDao()).thenReturn(configurationDao);
		when(configurationDao.snapshotFor(PROCESSING_ID)).thenReturn(configuration);
	}

	private void mockEntities() {
		Repository repository = mock(Repository.class);
		processing = mock(Processing.class);
		softwareModule = new Module(SOFTWARE, "null");
		softwareResult = new ModuleResult(null, new Module(SOFTWARE, REPOSITORY_NAME));
		classModule = new Module(CLASS, "HelloWorld");
		classResult = new ModuleResult(softwareResult, classModule);

		doReturn(processing).when(analyzingTask).processing();
		doReturn(repository).when(analyzingTask).repository();
		when(processing.getId()).thenReturn(PROCESSING_ID);
		when(repository.getName()).thenReturn(REPOSITORY_NAME);
	}

	private void mockSourceTreeBuilder() throws Exception {
		treeBuilder = mock(SourceTreeBuilder.class);
		whenNew(SourceTreeBuilder.class)
			.withArguments(PROCESSING_ID, REPOSITORY_NAME, moduleResultDao).thenReturn(treeBuilder);
		when(treeBuilder.save(softwareModule)).thenReturn(ROOT_ID);
		when(treeBuilder.save(classModule)).thenReturn(CLASS_ID);
	}

	private void mockModuleResultConfigurer() throws Exception {
		configurer = mock(ModuleResultConfigurer.class);
		whenNew(ModuleResultConfigurer.class)
			.withArguments(processing, configuration, metricResultDao, moduleResultDao).thenReturn(configurer);
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
	public void shouldAddModulesToSourceTree() {
		analyzingTask.perform();
		verify(treeBuilder).save(softwareModule);
		verify(treeBuilder).save(classModule);
	}

	@Test
	public void shouldAddMetricResults() {
		NativeMetric cbo = loadFixture("cbo", NativeMetric.class);
		NativeMetric lcom4 = loadFixture("lcom4", NativeMetric.class);
		NativeMetric totalCof = loadFixture("total_cof", NativeMetric.class);

		analyzingTask.perform();
		verify(metricResultDao).save(new MetricResult(configuration.getConfigurationFor(totalCof), 0.0), ROOT_ID);
		verify(metricResultDao).save(new MetricResult(configuration.getConfigurationFor(cbo), 0.0), CLASS_ID);
		verify(metricResultDao).save(new MetricResult(configuration.getConfigurationFor(lcom4), 1.0), CLASS_ID);
	}

	@Test
	public void shouldConfigureByHeight() {
		when(treeBuilder.getMaximumHeight()).thenReturn(1);
		when(moduleResultDao.getResultsAtHeight(1, PROCESSING_ID)).thenReturn(list(classResult));
		when(moduleResultDao.getResultsAtHeight(0, PROCESSING_ID)).thenReturn(list(softwareResult));

		analyzingTask.perform();
		InOrder order = Mockito.inOrder(configurer);
		order.verify(configurer).configure(classResult);
		order.verify(configurer).configure(softwareResult);
	}
}