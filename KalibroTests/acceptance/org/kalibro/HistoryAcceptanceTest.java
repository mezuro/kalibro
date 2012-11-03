package org.kalibro;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.experimental.theories.Theory;
import org.kalibro.tests.AcceptanceTest;

public class HistoryAcceptanceTest extends AcceptanceTest {

	private static final Long SLEEP = 2000L;

	private CompoundMetric metric;
	private Configuration configuration;

	private Repository repository;
	private Processing first, second, third;

	@Before
	public void setUp() {
		metric = new CompoundMetric();
		metric.setScript("return 1;");
		configuration = new Configuration();
		configuration.addMetricConfiguration(new MetricConfiguration(metric));

		String address = repositoriesDirectory().getAbsolutePath() + "/HelloWorldDirectory/";
		repository = new Repository("Repository name", RepositoryType.LOCAL_DIRECTORY, address);
		repository.setConfiguration(configuration);
		new Project().addRepository(repository);
	}

	@Theory
	public void shouldBrowseProcessHistory(SupportedDatabase databaseType) throws Exception {
		prepareProcessings(databaseType);

		assertTrue(Processing.hasProcessingAfter(first.getDate(), repository));
		assertTrue(Processing.hasProcessingAfter(second.getDate(), repository));
		assertFalse(Processing.hasProcessingAfter(third.getDate(), repository));

		assertFalse(Processing.hasProcessingBefore(first.getDate(), repository));
		assertTrue(Processing.hasProcessingBefore(second.getDate(), repository));
		assertTrue(Processing.hasProcessingBefore(third.getDate(), repository));

		assertDeepEquals(first, Processing.firstProcessing(repository));
		assertDeepEquals(third, Processing.lastProcessing(repository));

		assertDeepEquals(second, Processing.firstProcessingAfter(first.getDate(), repository));
		assertDeepEquals(third, Processing.firstProcessingAfter(second.getDate(), repository));

		assertDeepEquals(second, Processing.lastProcessingBefore(third.getDate(), repository));
		assertDeepEquals(first, Processing.lastProcessingBefore(second.getDate(), repository));
	}

	private void prepareProcessings(SupportedDatabase databaseType) throws Exception {
		resetDatabase(databaseType);
		repository.process();
		repository.process();
		repository.process();
		Thread.sleep(SLEEP);
		first = Processing.firstProcessing(repository);
		second = Processing.firstProcessingAfter(first.getDate(), repository);
		third = Processing.firstProcessingAfter(second.getDate(), repository);
	}
}