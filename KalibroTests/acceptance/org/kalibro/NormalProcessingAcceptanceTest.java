package org.kalibro;

import static org.junit.Assert.*;
import static org.kalibro.Granularity.*;
import static org.kalibro.ProcessState.*;

import org.junit.Before;
import org.junit.experimental.theories.Theory;
import org.kalibro.tests.AcceptanceTest;

public class NormalProcessingAcceptanceTest extends AcceptanceTest {

	private static final Long SLEEP = 10000L;
	private static final String REPOSITORY_NAME = "HelloWorldDirectory";

	private Metric totalCof, cbo, lcom4, sc;
	private Configuration configuration;

	private Repository repository;
	private Processing processing;

	@Before
	public void setUp() {
		loadConfigurationFixtures();
		String address = repositoriesDirectory().getAbsolutePath() + "/HelloWorldDirectory/";
		repository = new Repository(REPOSITORY_NAME, RepositoryType.LOCAL_DIRECTORY, address);
		repository.setConfiguration(configuration);
		new Project("Hello World").addRepository(repository);
	}

	private void loadConfigurationFixtures() {
		totalCof = loadFixture("total_cof", NativeMetric.class);
		cbo = loadFixture("cbo", NativeMetric.class);
		lcom4 = loadFixture("lcom4", NativeMetric.class);
		sc = loadFixture("sc", CompoundMetric.class);
		configuration = loadFixture("sc-analizo", Configuration.class);
	}

	@Theory
	public void shouldProcessNormally(SupportedDatabase databaseType) throws InterruptedException {
		resetDatabase(databaseType);
		assertFalse(Processing.hasProcessing(repository));

		long processingTime = System.currentTimeMillis();
		repository.process();
		verifyProcessOngoing();

		Thread.sleep(SLEEP);

		verifyProcessDone(processingTime);
		verifyResults();
	}

	private void verifyProcessOngoing() {
		assertTrue(Processing.hasProcessing(repository));
		assertFalse(Processing.hasReadyProcessing(repository));
		assertTrue(Processing.lastProcessingState(repository).isTemporary());
	}

	private void verifyProcessDone(long processingTime) {
		processing = Processing.lastProcessing(repository);
		assertEquals(processingTime, processing.getDate().getTime(), 500);
		assertEquals(READY, processing.getState());
		verifyStateTime(LOADING);
		verifyStateTime(COLLECTING);
		verifyStateTime(ANALYZING);
	}

	private void verifyStateTime(ProcessState state) {
		Long stateTime = processing.getStateTime(state);
		assertTrue("Time for " + state + ": " + stateTime, 0 < stateTime && stateTime < SLEEP);
	}

	private void verifyResults() {
		ModuleResult root = processing.getResultsRoot();
		assertFalse(root.hasParent());
		verifySoftwareResult(root);

		assertEquals(1, root.getChildren().size());
		verifyClassResult(root.getChildren().first());
	}

	private void verifySoftwareResult(ModuleResult root) {
		assertDeepEquals(new Module(SOFTWARE, REPOSITORY_NAME), root.getModule());
		assertDoubleEquals(10.0, root.getGrade());

		assertDoubleEquals(1.0, root.getResultFor(totalCof).getValue());
		assertFalse(root.getResultFor(totalCof).hasRange());
		verifyClassMetricResults(root);
	}

	private void verifyClassResult(ModuleResult child) {
		assertDeepEquals(new Module(CLASS, "HelloWorld"), child.getModule());
		assertDoubleEquals(10.0, child.getGrade());

		assertFalse(child.hasResultFor(totalCof));
		verifyClassMetricResults(child);
	}

	private void verifyClassMetricResults(ModuleResult moduleResult) {
		assertDoubleEquals(0.0, moduleResult.getResultFor(cbo).getAggregatedValue());
		assertDoubleEquals(1.0, moduleResult.getResultFor(lcom4).getAggregatedValue());
		assertDoubleEquals(0.0, moduleResult.getResultFor(sc).getAggregatedValue());

		assertFalse(moduleResult.getResultFor(cbo).hasRange());
		assertTrue(moduleResult.getResultFor(lcom4).hasRange());
		assertFalse(moduleResult.getResultFor(sc).hasRange());
	}
}