package org.kalibro;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.experimental.theories.Theory;
import org.kalibro.core.concurrent.VoidTask;
import org.kalibro.tests.AcceptanceTest;

public class ProcessingObserverAcceptanceTest extends AcceptanceTest {

	private ProcessingObserver processingObserver;
	private Repository repository;
	private Project project;
	private Configuration configuration;

	@Before
	public void setUp() {
		configuration = loadFixture("sc-analizo", Configuration.class);
		String address = repositoriesDirectory().getAbsolutePath() + "/HelloWorldBazaar/";
		repository = new Repository("Bazaar", RepositoryType.BAZAAR, address);
		repository.setConfiguration(configuration);
		project = new Project("Hello World");
		project.setDescription("Sample project");
		project.addRepository(repository);
		processingObserver = new ProcessingObserver("ProcessingObserverAcceptanceTest name",
			"ProcessingObserverAcceptanceTest email");
	}

	@Theory
	public void testCrud(SupportedDatabase databaseType) {
		resetDatabase(databaseType);
		assertNotSaved();

		processingObserver.save(repository);
		assertSaved();

		processingObserver.setName("Customized name");
		assertFalse(ProcessingObserver.all().first().deepEquals(processingObserver));

		processingObserver.save(repository);
		assertSaved();

		processingObserver.delete();
		assertNotSaved();
	}

	private void assertNotSaved() {
		assertTrue(ProcessingObserver.all().isEmpty());
	}

	private void assertSaved() {
		assertDeepEquals(set(processingObserver), ProcessingObserver.all());
	}

	@Theory
	public void shouldValidateOnSave(SupportedDatabase databaseType) {
		resetDatabase(databaseType);
		nameShouldBeRequired();
		emailShouldBeRequired();
		repositoryShouldNotBeNull();
	}

	private void nameShouldBeRequired() {
		String name = processingObserver.getName();
		processingObserver.setName(" ");
		assertThat(new VoidTask() {

			@Override
			protected void perform() {
				processingObserver.save(repository);
			}
		}).throwsException().withMessage("ProcessingObserver requires name.");
		processingObserver.setName(name);
	}

	private void emailShouldBeRequired() {
		String email = processingObserver.getEmail();
		processingObserver.setEmail(" ");
		assertThat(new VoidTask() {

			@Override
			protected void perform() {
				processingObserver.save(repository);
			}
		}).throwsException().withMessage("ProcessingObserver requires email.");
		processingObserver.setEmail(email);
	}

	private void repositoryShouldNotBeNull() {
		assertThat(new VoidTask() {

			@Override
			protected void perform() {
				processingObserver.save(null);
			}
		}).throwsException().withMessage("ProcessingObserver is not related to any repository.");
	}

}
