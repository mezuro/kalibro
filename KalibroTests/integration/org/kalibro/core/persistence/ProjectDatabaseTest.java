package org.kalibro.core.persistence;

import static org.junit.Assert.*;
import static org.kalibro.core.model.ConfigurationFixtures.kalibroConfiguration;
import static org.kalibro.core.model.ProjectFixtures.newHelloWorld;
import static org.kalibro.core.model.ProjectResultFixtures.newHelloWorldResult;

import javax.persistence.NoResultException;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.core.concurrent.Task;
import org.kalibro.core.model.Project;
import org.kalibro.core.model.ProjectResult;
import org.kalibro.core.persistence.dao.ProjectDao;

public abstract class ProjectDatabaseTest extends DatabaseTestCase {

	private ProjectDao dao;

	private Project helloWorld, helloWorld2;
	private ProjectResult projectResult;

	@Before
	public void setUp() {
		projectResult = newHelloWorldResult();
		helloWorld = projectResult.getProject();
		helloWorld2 = newHelloWorld();
		helloWorld2.setName("HelloWorld-2.0");
		daoFactory.createConfigurationDao().save(kalibroConfiguration());
		dao = daoFactory.createProjectDao();
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void shouldListSavedProjectNames() {
		assertTrue(dao.getProjectNames().isEmpty());

		dao.save(helloWorld);
		assertDeepList(dao.getProjectNames(), helloWorld.getName());

		dao.save(helloWorld2);
		assertDeepList(dao.getProjectNames(), helloWorld.getName(), helloWorld2.getName());
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void shouldRetrieveSavedProject() {
		dao.save(helloWorld);
		Project retrieved = retrieve(helloWorld);
		assertNotSame(helloWorld, retrieved);
		assertDeepEquals(helloWorld, retrieved);
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void shouldRemoveProjectByName() {
		dao.save(helloWorld);
		dao.save(helloWorld2);
		assertDeepList(dao.getProjectNames(), helloWorld.getName(), helloWorld2.getName());

		dao.removeProject(helloWorld.getName());
		assertDeepList(dao.getProjectNames(), helloWorld2.getName());

		dao.removeProject(helloWorld2.getName());
		assertTrue(dao.getProjectNames().isEmpty());
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void projectRemovalShouldCascadeToResults() {
		String projectName = helloWorld.getName();
		ProjectResultDatabaseDao projectResultDao = daoFactory.createProjectResultDao();

		dao.save(helloWorld);
		projectResultDao.save(projectResult);
		assertTrue(projectResultDao.hasResultsFor(projectName));

		dao.removeProject(projectName);
		assertFalse(projectResultDao.hasResultsFor(projectName));
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void shouldNotRetrieveUnsavedProject() {
		checkKalibroException(new Task() {

			@Override
			public void perform() {
				retrieve(helloWorld);
			}
		}, "There is no project named '" + helloWorld.getName() + "'", NoResultException.class);
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void shouldSaveAndRetrieveError() {
		helloWorld.setError(new Exception("An error message"));
		dao.save(helloWorld);
		assertEquals("An error message", retrieve(helloWorld).getError().getMessage());

		helloWorld.setError(new Exception("Another error message"));
		dao.save(helloWorld);
		assertEquals("Another error message", retrieve(helloWorld).getError().getMessage());
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
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