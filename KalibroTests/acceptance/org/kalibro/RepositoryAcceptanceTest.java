package org.kalibro;

import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.experimental.theories.Theory;
import org.kalibro.core.concurrent.TaskMatcher;
import org.kalibro.core.concurrent.VoidTask;
import org.kalibro.tests.AcceptanceTest;

public class RepositoryAcceptanceTest extends AcceptanceTest {

	private Project project;
	private Repository repository;
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
	}

	@Theory
	public void testCrud(SupportedDatabase databaseType) {
		resetDatabase(databaseType);
		project.save();
		assertDeepEquals(repository, saved());

		repository.setName("RepositoryAcceptanceTest name");
		assertFalse(repository.deepEquals(saved()));

		repository.save();
		assertDeepEquals(repository, saved());

		repository.delete();
		assertFalse(project.getRepositories().contains(repository));
		assertFalse(Project.all().first().getRepositories().contains(repository));
	}

	private Repository saved() {
		return Project.all().first().getRepositories().first();
	}

	@Theory
	public void shouldValidateOnSave(SupportedDatabase databaseType) {
		resetDatabase(databaseType);
		shouldBeInProject();
		nameShouldBeRequired();
		addressShouldBeRequired();
		configurationShouldBeRequired();
	}

	private void shouldBeInProject() {
		project.removeRepository(repository);
		assertSave().throwsException().withMessage("Repository is not in any project.");
		project.addRepository(repository);
	}

	private void nameShouldBeRequired() {
		repository.setName(" ");
		assertSave().throwsException().withMessage("Repository requires name.");
		repository.setName("name");
	}

	private void addressShouldBeRequired() {
		repository.setAddress(" ");
		assertSave().throwsException().withMessage("Repository requires address.");
		repository.setAddress("address");
	}

	private void configurationShouldBeRequired() {
		repository.setConfiguration(null);
		assertSave().throwsException().withMessage("A configuration should be associated with the repository.");
	}

	private TaskMatcher assertSave() {
		return assertThat(new VoidTask() {

			@Override
			protected void perform() {
				repository.save();
			}
		});
	}
}