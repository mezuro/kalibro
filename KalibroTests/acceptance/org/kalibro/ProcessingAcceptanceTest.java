package org.kalibro;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.experimental.theories.Theory;
import org.junit.rules.Timeout;
import org.kalibro.tests.AcceptanceTest;

public class ProcessingAcceptanceTest extends AcceptanceTest {

	private Repository repository;
	private Configuration configuration;

	@Before
	public void setUp() {
		configuration = loadFixture("sc-analizo", Configuration.class);
		String address = repositoriesDirectory().getAbsolutePath() + "/HelloWorldDirectory/";
		repository = new Repository("Directory repository", RepositoryType.LOCAL_DIRECTORY, address);
		repository.setConfiguration(configuration);
		new Project("Hello World").addRepository(repository);
	}

	@Override
	protected Timeout testTimeout() {
		return new Timeout(0);
	}

	@Theory
	public void shouldProcessRepository(SupportedDatabase databaseType) throws InterruptedException {
		resetDatabase(databaseType);
		assertFalse(Processing.hasProcessing(repository));

		repository.process();
		assertTrue(Processing.hasProcessing(repository));
		assertFalse(Processing.hasReadyProcessing(repository));
		assertTrue(Processing.lastProcessingState(repository).isTemporary());
		Thread.sleep(10000);

		assertEquals(ProcessState.READY, Processing.lastProcessingState(repository));
	}
}