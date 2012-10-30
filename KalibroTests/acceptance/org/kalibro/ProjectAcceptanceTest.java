package org.kalibro;

import static org.junit.Assert.*;

import javax.persistence.RollbackException;

import org.junit.Before;
import org.junit.experimental.theories.Theory;
import org.kalibro.core.concurrent.TaskMatcher;
import org.kalibro.core.concurrent.VoidTask;
import org.kalibro.tests.AcceptanceTest;

public class ProjectAcceptanceTest extends AcceptanceTest {

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
		assertNotSaved();

		project.save();
		assertSaved();

		project.setName("ProjectAcceptanceTest name");
		assertFalse(Project.all().first().deepEquals(project));

		project.save();
		assertSaved();

		project.delete();
		assertNotSaved();
	}

	private void assertNotSaved() {
		assertTrue(Project.all().isEmpty());
	}

	private void assertSaved() {
		assertDeepEquals(set(project), Project.all());
	}

	@Theory
	public void shouldValidateOnSave(SupportedDatabase databaseType) {
		resetDatabase(databaseType);
		nameShouldBeUnique();
		nameShouldBeRequired();
	}

	private void nameShouldBeUnique() {
		new Project(project.getName()).save();
		assertSave().doThrow(RollbackException.class);
	}

	private void nameShouldBeRequired() {
		project.setName(" ");
		assertSave().throwsException().withMessage("Project requires name.");
	}

	private TaskMatcher assertSave() {
		return assertThat(new VoidTask() {

			@Override
			protected void perform() {
				project.save();
			}
		});
	}

	@Theory
	public void shouldCascadeSaveButNotDeleteToConfigurations(SupportedDatabase databaseType) {
		resetDatabase(databaseType);
		assertTrue(Configuration.all().isEmpty());
		project.save();
		assertDeepEquals(set(configuration), Configuration.all());
		project.delete();
		assertDeepEquals(set(configuration), Configuration.all());
	}
}