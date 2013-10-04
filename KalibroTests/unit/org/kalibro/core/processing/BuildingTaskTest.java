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
@PrepareForTest({ModuleResult.class, BuildingTask.class})
public class BuildingTaskTest extends UnitTest {

	private static final Long RESULT_ID = new Random().nextLong();
	private static final Long PROCESSING_ID = new Random().nextLong();
	private static final String REPOSITORY_NAME = "BuildingTaskTest repository name";

	private ModuleResultDatabaseDao dao;
	private ModuleResult result;

	private BuildingTask treeBuilder;

	@Before
	public void setUp() {
		dao = mock(ModuleResultDatabaseDao.class);
		result = mock(ModuleResult.class);
		when(result.getId()).thenReturn(RESULT_ID);
		when(dao.save(result, PROCESSING_ID)).thenReturn(result);
		treeBuilder = new BuildingTask(PROCESSING_ID, REPOSITORY_NAME, dao);
	}

	@Test
	public void shouldSaveRootWithRepositoryName() throws Exception {
		Module rootModule = new Module(SOFTWARE, REPOSITORY_NAME);
		whenNew(ModuleResult.class).withArguments(null, rootModule).thenReturn(result);

		assertEquals(RESULT_ID, treeBuilder.save(new Module(SOFTWARE, "null")));
		verify(dao).save(result, PROCESSING_ID);
	}

	@Test
	public void shouldAppendAncestryToLastExistingAncestor() throws Exception {
		Module junit = new Module(PACKAGE, "org", "junit");
		Module org = junit.inferParent();
		Module root = org.inferParent();
		ModuleResult rootResult = new ModuleResult(null, root);
		ModuleResult orgResult = new ModuleResult(rootResult, org);

		when(dao.getResultFor(root, PROCESSING_ID)).thenReturn(rootResult);
		whenNew(ModuleResult.class).withArguments(rootResult, org).thenReturn(orgResult);
		when(dao.save(orgResult, PROCESSING_ID)).thenReturn(orgResult);
		whenNew(ModuleResult.class).withArguments(orgResult, junit).thenReturn(result);

		assertEquals(RESULT_ID, treeBuilder.save(junit));
		verify(dao, never()).save(rootResult, PROCESSING_ID);
		verify(dao).save(orgResult, PROCESSING_ID);
		verify(dao).save(result, PROCESSING_ID);
	}

	@Test
	public void shouldChangeGranularityOfExistingModuleResult() {
		Module newModule = new Module(CLASS, "org", "junit", "Test");
		Module existingModule = mock(Module.class);
		when(dao.getResultFor(newModule, PROCESSING_ID)).thenReturn(result);
		when(result.getModule()).thenReturn(existingModule);
		when(existingModule.getGranularity()).thenReturn(PACKAGE);

		assertEquals(RESULT_ID, treeBuilder.save(newModule));
		verify(existingModule).setGranularity(CLASS);
		verify(dao).save(result, PROCESSING_ID);
	}

	@Test
	public void shouldRetrieveMaximumHeight() {
		Module root = new Module(SOFTWARE, REPOSITORY_NAME);
		Module leaf = new Module(CLASS, "org", "kalibro", "core", "processing", "BuildingTaskTest");
		when(dao.getResultFor(root, PROCESSING_ID)).thenReturn(new ModuleResult(null, root));
		when(dao.getResultFor(leaf, PROCESSING_ID)).thenReturn(result);
		when(result.getModule()).thenReturn(leaf);
		when(result.getHeight()).thenReturn(5);

		treeBuilder.save(root);
		treeBuilder.save(leaf);
		assertEquals(5, treeBuilder.getMaximumHeight());
	}

	private static final Long ROOT_ID = new Random().nextLong();
	private static final Long CLASS_ID = new Random().nextLong();
	private static final Long PROCESSING_ID = new Random().nextLong();
	private static final String REPOSITORY_NAME = "AggregatingTaskTest repository name";

	private Processing processing;
	private Configuration configuration;
	private BuildingTask treeBuilder;
	private CalculatingTask configurer;
	private MetricResultDatabaseDao metricResultDao;
	private ModuleResultDatabaseDao moduleResultDao;

	private Module softwareModule, classModule;
	private ModuleResult softwareResult, classResult;

	private AggregatingTask aggregatingTask;

	@Before
	public void setUp() throws Exception {
		aggregatingTask = spy(new AggregatingTask());
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
		treeBuilder = mock(BuildingTask.class);
		DatabaseDaoFactory daoFactory = mock(DatabaseDaoFactory.class);
		ConfigurationDatabaseDao configurationDao = mock(ConfigurationDatabaseDao.class);

		doReturn(daoFactory).when(aggregatingTask).daoFactory();
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

		doReturn(processing).when(aggregatingTask).processing();
		doReturn(repository).when(aggregatingTask).repository();
		when(processing.getId()).thenReturn(PROCESSING_ID);
		when(repository.getName()).thenReturn(REPOSITORY_NAME);
	}

	private void mockSourceTreeBuilder() throws Exception {
		treeBuilder = mock(BuildingTask.class);
		whenNew(BuildingTask.class)
			.withArguments(PROCESSING_ID, REPOSITORY_NAME, moduleResultDao).thenReturn(treeBuilder);
		when(treeBuilder.save(softwareModule)).thenReturn(ROOT_ID);
		when(treeBuilder.save(classModule)).thenReturn(CLASS_ID);
	}

	private void mockModuleResultConfigurer() throws Exception {
		configurer = mock(CalculatingTask.class);
		whenNew(CalculatingTask.class)
			.withArguments(processing, configuration, metricResultDao, moduleResultDao).thenReturn(configurer);
	}

	private void stubResultProducer() {
		Producer<NativeModuleResult> resultProducer = new Producer<NativeModuleResult>();
		Writer<NativeModuleResult> writer = resultProducer.createWriter();
		writer.write(newResult(classModule, "cbo", 0.0, "lcom4", 1.0));
		writer.write(newResult(softwareModule, "total_cof", 1.0));
		writer.close();
		doReturn(resultProducer).when(aggregatingTask).resultProducer();
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
		aggregatingTask.perform();
		verify(treeBuilder).save(softwareModule);
		verify(treeBuilder).save(classModule);
	}

	@Test
	public void shouldAddMetricResults() {
		NativeMetric cbo = loadFixture("cbo", NativeMetric.class);
		NativeMetric lcom4 = loadFixture("lcom4", NativeMetric.class);
		NativeMetric totalCof = loadFixture("total_cof", NativeMetric.class);

		aggregatingTask.perform();
		verify(metricResultDao).saveAll(list(
			new MetricResult(configuration.getConfigurationFor(totalCof), 0.0)), ROOT_ID);
		verify(metricResultDao).saveAll(list(
			new MetricResult(configuration.getConfigurationFor(cbo), 0.0),
			new MetricResult(configuration.getConfigurationFor(lcom4), 1.0)), CLASS_ID);
	}

	@Test
	public void shouldConfigureResults() {
		when(moduleResultDao.getResultsOfProcessing(PROCESSING_ID)).thenReturn(list(softwareResult, classResult));

		aggregatingTask.perform();
		verify(configurer).configure(softwareResult);
		verify(configurer).configure(classResult);
	}
}