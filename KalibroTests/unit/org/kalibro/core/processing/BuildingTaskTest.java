package org.kalibro.core.processing;

import static org.kalibro.Granularity.*;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.*;
import org.kalibro.core.concurrent.Producer;
import org.kalibro.core.concurrent.Writer;
import org.kalibro.core.persistence.MetricResultDatabaseDao;
import org.kalibro.core.persistence.ModuleResultDatabaseDao;
import org.kalibro.tests.UnitTest;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({BuildingTask.class, ModuleResult.class})
public class BuildingTaskTest extends UnitTest {

	private static final long PROCESSING_ID = new Random().nextLong();
	private static final String REPOSITORY_NAME = "BuildingTaskTest repository name";

	private Configuration configuration;
	private ModuleResultDatabaseDao moduleResultDao;
	private MetricResultDatabaseDao metricResultDao;
	private Producer<NativeModuleResult> resultProducer;

	private BuildingTask buildingTask;

	@Before
	public void setUp() {
		ProcessContext context = mockContext();
		mockDaosAndProducer(context);
		buildingTask = new BuildingTask(context);
	}

	private ProcessContext mockContext() {
		Processing processing = mock(Processing.class);
		Repository repository = mock(Repository.class);
		ProcessContext context = new ProcessContext(repository);
		configuration = loadFixture("sc", Configuration.class);
		when(processing.getId()).thenReturn(PROCESSING_ID);
		when(repository.getName()).thenReturn(REPOSITORY_NAME);
		context.processing = processing;
		context.configuration = configuration;
		return context;
	}

	private void mockDaosAndProducer(ProcessContext context) {
		moduleResultDao = mock(ModuleResultDatabaseDao.class);
		metricResultDao = mock(MetricResultDatabaseDao.class);
		resultProducer = new Producer<NativeModuleResult>();
		context.moduleResultDao = moduleResultDao;
		context.metricResultDao = metricResultDao;
		context.resultProducer = resultProducer;
	}

	@Test
	public void shouldSaveRootWithRepositoryName() throws Throwable {
		ModuleResult result = mock(ModuleResult.class);
		whenNew(ModuleResult.class).withArguments(null, new Module(SOFTWARE, REPOSITORY_NAME)).thenReturn(result);
		when(moduleResultDao.save(result, PROCESSING_ID)).thenReturn(result);

		executeWithResults(new NativeModuleResult(new Module(SOFTWARE, "null")));
		verify(moduleResultDao).save(result, PROCESSING_ID);
	}

	@Test
	public void shouldAppendAncestryToLastExistingAncestor() throws Throwable {
		Module junit = new Module(PACKAGE, "org", "junit");
		Module org = junit.inferParent();
		Module root = org.inferParent();
		ModuleResult rootResult = new ModuleResult(null, root);
		ModuleResult orgResult = new ModuleResult(rootResult, org);
		ModuleResult junitResult = new ModuleResult(orgResult, junit);

		when(moduleResultDao.getResultFor(root, PROCESSING_ID)).thenReturn(rootResult);
		whenNew(ModuleResult.class).withArguments(rootResult, org).thenReturn(orgResult);
		whenNew(ModuleResult.class).withArguments(orgResult, junit).thenReturn(junitResult);
		when(moduleResultDao.save(orgResult, PROCESSING_ID)).thenReturn(orgResult);
		when(moduleResultDao.save(junitResult, PROCESSING_ID)).thenReturn(junitResult);

		executeWithResults(new NativeModuleResult(junit));
		verify(moduleResultDao, never()).save(rootResult, PROCESSING_ID);
		verify(moduleResultDao).save(orgResult, PROCESSING_ID);
		verify(moduleResultDao).save(junitResult, PROCESSING_ID);
	}

	@Test
	public void shouldFixGranularityOfExistingModuleResult() throws Throwable {
		Module newModule = new Module(CLASS, "org", "junit", "Test");
		Module existingModule = mock(Module.class);
		ModuleResult result = mock(ModuleResult.class);
		when(moduleResultDao.getResultFor(newModule, PROCESSING_ID)).thenReturn(result);
		when(result.getModule()).thenReturn(existingModule);
		when(existingModule.getGranularity()).thenReturn(PACKAGE);
		when(moduleResultDao.save(result, PROCESSING_ID)).thenReturn(result);

		executeWithResults(new NativeModuleResult(newModule));
		verify(existingModule).setGranularity(CLASS);
		verify(moduleResultDao).save(result, PROCESSING_ID);
	}

	@Test
	public void shouldAddMetricResults() throws Throwable {
		NativeMetric cbo = loadFixture("cbo", NativeMetric.class);
		NativeMetric lcom4 = loadFixture("lcom4", NativeMetric.class);
		NativeModuleResult nativeResult = new NativeModuleResult(new Module(SOFTWARE, "HelloWorld"));
		nativeResult.addMetricResult(new NativeMetricResult(cbo, 1.0));
		nativeResult.addMetricResult(new NativeMetricResult(lcom4, 2.0));
		ModuleResult result = new ModuleResult(null, nativeResult.getModule());
		when(moduleResultDao.save(any(ModuleResult.class), eq(PROCESSING_ID))).thenReturn(result);

		executeWithResults(nativeResult);
		verify(metricResultDao).saveAll(list(
			new MetricResult(configuration.getConfigurationFor(cbo), 1.0),
			new MetricResult(configuration.getConfigurationFor(lcom4), 2.0)), result.getId());
	}

	private void executeWithResults(NativeModuleResult... results) throws Throwable {
		Writer<NativeModuleResult> writer = resultProducer.createWriter();
		for (NativeModuleResult result : results)
			writer.write(result);
		writer.close();
		buildingTask.perform();
	}
}