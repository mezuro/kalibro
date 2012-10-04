package org.kalibro.core.persistence;

import static org.junit.Assert.*;
import static org.kalibro.ConfigurationFixtures.kalibroConfiguration;
import static org.kalibro.ProjectFixtures.newHelloWorld;
import static org.kalibro.ProjectResultFixtures.newHelloWorldResult;

import javax.persistence.NoResultException;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.Project;
import org.kalibro.RepositoryResult;
import org.kalibro.core.concurrent.VoidTask;
import org.kalibro.dao.DaoFactory;
import org.kalibro.dao.ProjectDao;
import org.kalibro.dao.ProjectResultDao;
import org.kalibro.tests.AcceptanceTest;

public class ProjectDatabaseTest extends AcceptanceTest {

	private ProjectDao dao;

	private Project helloWorld, helloWorld2;
	private RepositoryResult repositoryResult;

	@Before
	public void setUp() {
		repositoryResult = newHelloWorldResult();
		helloWorld = repositoryResult.getProject();
		helloWorld2 = newHelloWorld();
		helloWorld2.setName("HelloWorld-2.0");
		DaoFactory.getConfigurationDao().save(kalibroConfiguration());
		dao = DaoFactory.getProjectDao();
	}

	@Test
	public void shouldListSavedProjectNames() {
		assertTrue(dao.getProjectNames().isEmpty());

		dao.save(helloWorld);
		assertDeepEquals(asList(helloWorld.getName()), dao.getProjectNames());

		dao.save(helloWorld2);
		assertDeepEquals(asList(helloWorld.getName(), helloWorld2.getName()), dao.getProjectNames());
	}

	@Test
	public void shouldRetrieveSavedProject() {
		dao.save(helloWorld);
		Project retrieved = retrieve(helloWorld);
		assertNotSame(helloWorld, retrieved);
		assertDeepEquals(helloWorld, retrieved);
	}

	@Test
	public void shouldRemoveProjectByName() {
		dao.save(helloWorld);
		dao.save(helloWorld2);
		assertDeepEquals(asList(helloWorld.getName(), helloWorld2.getName()), dao.getProjectNames());

		dao.removeProject(helloWorld.getName());
		assertDeepEquals(asList(helloWorld2.getName()), dao.getProjectNames());

		dao.removeProject(helloWorld2.getName());
		assertTrue(dao.getProjectNames().isEmpty());
	}

	@Test
	public void projectRemovalShouldCascadeToResults() {
		String projectName = helloWorld.getName();
		ProjectResultDao projectResultDao = DaoFactory.getProjectResultDao();

		dao.save(helloWorld);
		projectResultDao.save(repositoryResult);
		assertTrue(projectResultDao.hasResultsFor(projectName));

		dao.removeProject(projectName);
		assertFalse(projectResultDao.hasResultsFor(projectName));
	}

	@Test
	public void shouldNotRetrieveUnsavedProject() {
		assertThat(new VoidTask() {

			@Override
			protected void perform() {
				retrieve(helloWorld);
			}
		}).throwsException().withMessage("There is no project named '" + helloWorld.getName() + "'")
			.withCause(NoResultException.class);
	}

	@Test
	public void shouldSaveAndRetrieveError() {
		helloWorld.setError(new Exception("An error message"));
		dao.save(helloWorld);
		assertEquals("An error message", retrieve(helloWorld).getError().getMessage());

		helloWorld.setError(new Exception("Another error message"));
		dao.save(helloWorld);
		assertEquals("Another error message", retrieve(helloWorld).getError().getMessage());
	}

	@Test
	public void projectsShouldNotShareRepositories() {
		assertDeepEquals(helloWorld.getRepository(), helloWorld2.getRepository());
		dao.save(helloWorld);
		dao.save(helloWorld2);

		helloWorld2.getRepository().setUsername("hello-world-user");
		dao.save(helloWorld2);

		assertEquals("", retrieve(helloWorld).getRepository().getUsername());
	}

	private Project retrieve(Project project) {
		return dao.getProject(project.getName());
	}
}