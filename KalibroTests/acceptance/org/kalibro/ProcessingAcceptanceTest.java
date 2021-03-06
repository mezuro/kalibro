package org.kalibro;

import static org.junit.Assert.*;
import static org.kalibro.Granularity.*;
import static org.kalibro.ProcessState.*;

import java.io.File;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.Theory;
import org.kalibro.core.Environment;
import org.kalibro.dao.DaoFactory;
import org.kalibro.dao.ProcessingDao;
import org.kalibro.tests.AcceptanceTest;
import org.powermock.reflect.Whitebox;

public class ProcessingAcceptanceTest extends AcceptanceTest {

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

	@After
	public void tearDown() {
		new File("notificationData.txt").delete();
	}

	private void loadConfigurationFixtures() {
		totalCof = loadFixture("total_cof", NativeMetric.class);
		cbo = loadFixture("cbo", NativeMetric.class);
		lcom4 = loadFixture("lcom4", NativeMetric.class);
		sc = loadFixture("sc", CompoundMetric.class);
		configuration = loadFixture("sc-analizo", Configuration.class);
	}

	@Theory
	public void shouldProcessNormally(SupportedDatabase databaseType) throws Exception {
		resetDatabase(databaseType);
		assertFalse(Processing.hasProcessing(repository));

		process();
		verifyProcessDone();
		verifyResults();

		shouldAllowChangingRepositoryType();
	}

	private void shouldAllowChangingRepositoryType() throws InterruptedException {
		repository.setType(RepositoryType.GIT);
		repository.setAddress(repositoriesDirectory().getAbsolutePath() + "/HelloWorldGit/");
		process();
		verifyProcessDone();
	}

	private void verifyProcessDone() {
		processing = Processing.lastProcessing(repository);
		long start = processing.getDate().getTime();
		long now = System.currentTimeMillis();
		long totalTime = now - start;
		assertTrue(totalTime > 0);
		assertProcessingReady();
		verifyStateTime(LOADING, totalTime);
		verifyStateTime(COLLECTING, totalTime);
		verifyStateTime(BUILDING, totalTime);
		verifyStateTime(AGGREGATING, totalTime);
		verifyStateTime(CALCULATING, totalTime);
	}

	private void verifyStateTime(ProcessState state, long totalTime) {
		Long stateTime = processing.getStateTime(state);
		assertTrue(0 < stateTime && stateTime < totalTime);
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

	@Theory
	public void shouldRetrieveErrorLoading(SupportedDatabase databaseType) throws Exception {
		resetDatabase(databaseType);
		repository.setAddress("/invalid/address/");
		process();

		processing = Processing.lastProcessing(repository);
		assertEquals(ERROR, processing.getState());
		assertEquals(LOADING, processing.getStateWhenErrorOcurred());
		assertNull(processing.getStateTime(COLLECTING));
		assertNull(processing.getStateTime(BUILDING));
		assertNull(processing.getStateTime(AGGREGATING));
		assertNull(processing.getStateTime(CALCULATING));
		assertNull(processing.getResultsRoot());
		assertEquals("Error while executing command: cp -ru /invalid/address/ .", processing.getError().getMessage());
	}

	@Theory
	public void shouldRetrieveErrorCalculatingCompoundMetric(SupportedDatabase databaseType) throws Exception {
		resetDatabase(databaseType);
		configuration.getCompoundMetrics().first().setScript("return cbo == 0 ? null : cbo;");
		process();

		processing = Processing.lastProcessing(repository);
		assertProcessingReady();

		ModuleResult root = processing.getResultsRoot();
		verifyScError(root);
		verifyScError(root.getChildren().first());
	}

	private void assertProcessingReady() {
		if (processing.getState() == ERROR)
			throw new KalibroException("Error while " + processing.getStateWhenErrorOcurred(), processing.getError());
		assertEquals(READY, processing.getState());
	}

	private void verifyScError(ModuleResult moduleResult) {
		assertTrue(moduleResult.getResultFor(sc).hasError());
		assertEquals("Error evaluating Javascript for: sc", moduleResult.getResultFor(sc).getError().getMessage());
	}

	@Test
	public void shouldExecuteNoficationCommand() throws Exception {
		resetDatabase(randomElementFrom(SupportedDatabase.values()));
		assertFalse(Processing.hasProcessing(repository));

		KalibroSettings settings = KalibroSettings.load();
		settings.getServerSettings().setNotificationCommand(
			Environment.dotKalibro() + "/notification.sh");
		settings.save();

		process();
		File notificationFile = new File("notificationData.txt");
		assertTrue(notificationFile.exists());
		assertEquals(repository.getId() + "\n", FileUtils.readFileToString(notificationFile));
	}

	@Theory
	public void deleteProjectShouldCascadeToProcessings(SupportedDatabase databaseType) throws Exception {
		resetDatabase(databaseType);
		repository.setAddress("/invalid/address/");
		process();
		assertTrue(Processing.hasProcessing(repository));

		repository.getProject().delete();
		assertTrue(allProcessings().isEmpty());
	}

	private Set<Processing> allProcessings() throws Exception {
		ProcessingDao processingDao = DaoFactory.getProcessingDao();
		Set<Processing> allProcessings = Whitebox.invokeMethod(processingDao, "all");
		return allProcessings;
	}

	private void process() throws InterruptedException {
		repository.process();
		do
			Thread.sleep(2000);
		while (isProcessOngoing());
	}

	private boolean isProcessOngoing() {
		return !Processing.hasProcessing(repository) || Processing.lastProcessingState(repository).isTemporary();
	}
}