package org.kalibro;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.experimental.theories.Theory;
import org.kalibro.core.concurrent.VoidTask;
import org.kalibro.tests.AcceptanceTest;

public class RepositoryObserverAcceptanceTest extends AcceptanceTest {

	private RepositoryObserver repositoryObserver;
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
		repositoryObserver = new RepositoryObserver("RepositoryObserverAcceptanceTest name",
			"RepositoryObserverAcceptanceTest email");
	}

	@Theory
	public void testCrud(SupportedDatabase databaseType) {
		resetDatabase(databaseType);
		assertNotSaved();

		repositoryObserver.save(repository);
		assertSaved();

		repositoryObserver.setName("Customized name");
		assertFalse(RepositoryObserver.all().first().deepEquals(repositoryObserver));

		repositoryObserver.save(repository);
		assertSaved();

		repositoryObserver.delete();
		assertNotSaved();
	}

	private void assertNotSaved() {
		assertTrue(RepositoryObserver.all().isEmpty());
	}

	private void assertSaved() {
		assertDeepEquals(set(repositoryObserver), RepositoryObserver.all());
	}

	@Theory
	public void shouldValidateOnSave(SupportedDatabase databaseType) {
		resetDatabase(databaseType);
		nameShouldBeRequired();
		emailShouldBeRequired();
		repositoryShouldNotBeNull();
	}

	private void nameShouldBeRequired() {
		String name = repositoryObserver.getName();
		repositoryObserver.setName(" ");
		assertThat(new VoidTask() {

			@Override
			protected void perform() {
				repositoryObserver.save(repository);
			}
		}).throwsException().withMessage("RepositoryObserver requires name.");
		repositoryObserver.setName(name);
	}

	private void emailShouldBeRequired() {
		String email = repositoryObserver.getEmail();
		repositoryObserver.setEmail(" ");
		assertThat(new VoidTask() {

			@Override
			protected void perform() {
				repositoryObserver.save(repository);
			}
		}).throwsException().withMessage("RepositoryObserver requires email.");
		repositoryObserver.setEmail(email);
	}

	private void repositoryShouldNotBeNull() {
		assertThat(new VoidTask() {

			@Override
			protected void perform() {
				repositoryObserver.save(null);
			}
		}).throwsException().withMessage("RepositoryObserver is not related to any repository.");
	}

}
