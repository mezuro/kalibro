package org.kalibro;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.experimental.theories.Theory;
import org.kalibro.core.concurrent.VoidTask;
import org.kalibro.tests.AcceptanceTest;

public class RepositorySubscriberAcceptanceTest extends AcceptanceTest {

	private RepositorySubscriber repositorySubscriber;
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
		repositorySubscriber = new RepositorySubscriber("RepositorySubscriberAcceptanceTest name",
			"RepositorySubscriberAcceptanceTest email");
	}

	@Theory
	public void testCrud(SupportedDatabase databaseType) {
		resetDatabase(databaseType);
		assertNotSaved();

		repositorySubscriber.save(repository);
		assertSaved();

		repositorySubscriber.setName("Customized name");
		assertFalse(RepositorySubscriber.all().first().deepEquals(repositorySubscriber));

		repositorySubscriber.save(repository);
		assertSaved();

		repositorySubscriber.delete();
		assertNotSaved();
	}

	private void assertNotSaved() {
		assertTrue(RepositorySubscriber.all().isEmpty());
	}

	private void assertSaved() {
		assertDeepEquals(set(repositorySubscriber), RepositorySubscriber.all());
	}

	@Theory
	public void shouldValidateOnSave(SupportedDatabase databaseType) {
		resetDatabase(databaseType);
		nameShouldBeRequired();
		emailShouldBeRequired();
		repositoryShouldNotBeNull();
	}

	private void nameShouldBeRequired() {
		String name = repositorySubscriber.getName();
		repositorySubscriber.setName(" ");
		assertThat(new VoidTask() {

			@Override
			protected void perform() {
				repositorySubscriber.save(repository);
			}
		}).throwsException().withMessage("Repository subscriber requires name.");
		repositorySubscriber.setName(name);
	}

	private void emailShouldBeRequired() {
		String email = repositorySubscriber.getEmail();
		repositorySubscriber.setEmail(" ");
		assertThat(new VoidTask() {

			@Override
			protected void perform() {
				repositorySubscriber.save(repository);
			}
		}).throwsException().withMessage("Repository subscriber requires email.");
		repositorySubscriber.setEmail(email);
	}

	private void repositoryShouldNotBeNull() {
		assertThat(new VoidTask() {

			@Override
			protected void perform() {
				repositorySubscriber.save(null);
			}
		}).throwsException().withMessage("Repository subscriber is not related to any repository.");
	}

}
