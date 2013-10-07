package org.kalibro;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.experimental.theories.Theory;
import org.kalibro.core.concurrent.VoidTask;
import org.kalibro.tests.AcceptanceTest;

public class RepositoryListenerAcceptanceTest extends AcceptanceTest {

	private RepositoryListener repositoryListener;
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
		repositoryListener = new RepositoryListener("RepositoryListenerAcceptanceTest name",
			"RepositoryListenerAcceptanceTest email");
	}

	@Theory
	public void testCrud(SupportedDatabase databaseType) {
		resetDatabase(databaseType);
		assertNotSaved();

		repositoryListener.save(repository);
		assertSaved();

		repositoryListener.setName("Customized name");
		assertFalse(RepositoryListener.all().first().deepEquals(repositoryListener));

		repositoryListener.save(repository);
		assertSaved();

		repositoryListener.delete();
		assertNotSaved();
	}

	private void assertNotSaved() {
		assertTrue(RepositoryListener.all().isEmpty());
	}

	private void assertSaved() {
		assertDeepEquals(set(repositoryListener), RepositoryListener.all());
	}

	@Theory
	public void shouldValidateOnSave(SupportedDatabase databaseType) {
		resetDatabase(databaseType);
		nameShouldBeRequired();
		emailShouldBeRequired();
		repositoryShouldNotBeNull();
	}

	private void nameShouldBeRequired() {
		String name = repositoryListener.getName();
		repositoryListener.setName(" ");
		assertThat(new VoidTask() {

			@Override
			protected void perform() {
				repositoryListener.save(repository);
			}
		}).throwsException().withMessage("RepositoryListener requires name.");
		repositoryListener.setName(name);
	}

	private void emailShouldBeRequired() {
		String email = repositoryListener.getEmail();
		repositoryListener.setEmail(" ");
		assertThat(new VoidTask() {

			@Override
			protected void perform() {
				repositoryListener.save(repository);
			}
		}).throwsException().withMessage("RepositoryListener requires email.");
		repositoryListener.setEmail(email);
	}

	private void repositoryShouldNotBeNull() {
		assertThat(new VoidTask() {

			@Override
			protected void perform() {
				repositoryListener.save(null);
			}
		}).throwsException().withMessage("RepositoryListener is not related to any repository.");
	}

}
