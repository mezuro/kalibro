package org.kalibro;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.SortedMap;

import org.junit.Before;
import org.junit.experimental.theories.Theory;
import org.kalibro.tests.AcceptanceTest;

public class HistoryAcceptanceTest extends AcceptanceTest {

	private static final Long SLEEP = 15000L;

	private CompoundMetric metric;
	private Configuration configuration;

	private Repository repository;
	private Processing first, second, third;

	@Before
	public void setUp() {
		configuration = loadFixture("sc-analizo", Configuration.class);
		metric = configuration.getCompoundMetrics().first();

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

		assertEquals(first, Processing.firstProcessing(repository));
		assertEquals(third, Processing.lastProcessing(repository));

		assertEquals(second, Processing.firstProcessingAfter(first.getDate(), repository));
		assertEquals(third, Processing.firstProcessingAfter(second.getDate(), repository));

		assertEquals(second, Processing.lastProcessingBefore(third.getDate(), repository));
		assertEquals(first, Processing.lastProcessingBefore(second.getDate(), repository));
	}

	@Theory
	public void shouldRetrieveMetricHistory(SupportedDatabase databaseType) throws Exception {
		prepareProcessings(databaseType);
		assertEquals(ProcessState.READY, third.getState());

		ModuleResult root = third.getResultsRoot();
		SortedMap<Date, MetricResult> history = root.historyOf(metric);
		assertDeepEquals(set(first.getDate(), second.getDate(), third.getDate()), history.keySet());
		assertDoubleEquals(0.0, history.get(first.getDate()).getAggregatedValue());
		assertDoubleEquals(0.0, history.get(second.getDate()).getAggregatedValue());
		assertDoubleEquals(0.0, history.get(third.getDate()).getAggregatedValue());
	}

	@Theory
	public void shouldRetrieveModuleResultHistory(SupportedDatabase databaseType) throws Exception {
		prepareProcessings(databaseType);
		assertEquals(ProcessState.READY, third.getState());

		ModuleResult root = third.getResultsRoot();
		SortedMap<Date, ModuleResult> history = root.history();
		assertDeepEquals(set(first.getDate(), second.getDate(), third.getDate()), history.keySet());
		assertDoubleEquals(10.0, history.get(first.getDate()).getGrade());
		assertDoubleEquals(10.0, history.get(second.getDate()).getGrade());
		assertDoubleEquals(10.0, history.get(third.getDate()).getGrade());		
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