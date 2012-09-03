package org.kalibro.core.persistence;

import static org.junit.Assert.*;
import static org.kalibro.core.model.ConfigurationFixtures.kalibroConfiguration;
import static org.kalibro.core.model.ProjectFixtures.helloWorld;
import static org.kalibro.core.model.ProjectResultFixtures.newHelloWorldResult;

import java.util.Date;

import javax.persistence.NoResultException;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.core.concurrent.Task;
import org.kalibro.core.model.Project;
import org.kalibro.core.model.ProjectResult;
import org.kalibro.core.persistence.dao.ProjectResultDao;

public abstract class ProjectResultDatabaseTest extends DatabaseTestCase {

	private ProjectResultDao dao;

	private Project project;
	private ProjectResult first, second, third;

	@Before
	public void setUp() {
		project = helloWorld();
		first = newHelloWorldResult(new Date(1));
		second = newHelloWorldResult(new Date(2));
		third = newHelloWorldResult(new Date(3));
		first.setProject(project);
		second.setProject(project);
		third.setProject(project);
		dao = daoFactory.getProjectResultDao();
		daoFactory.getConfigurationDao().save(kalibroConfiguration());
		daoFactory.getProjectDao().save(project);
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void testHasResults() {
		assertFalse(dao.hasResultsFor(project.getName()));
		saveResults();
		assertTrue(dao.hasResultsFor(project.getName()));
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void testHasResultsBefore() {
		saveResults();
		assertFalse(dao.hasResultsBefore(first.getDate(), project.getName()));
		assertTrue(dao.hasResultsBefore(second.getDate(), project.getName()));
		assertTrue(dao.hasResultsBefore(third.getDate(), project.getName()));
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void testHasResultsAfter() {
		saveResults();
		assertTrue(dao.hasResultsAfter(first.getDate(), project.getName()));
		assertTrue(dao.hasResultsAfter(second.getDate(), project.getName()));
		assertFalse(dao.hasResultsAfter(third.getDate(), project.getName()));
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void testFirstResult() {
		dao.save(third);
		assertDeepEquals(third, dao.getFirstResultOf(project.getName()));

		dao.save(second);
		assertDeepEquals(second, dao.getFirstResultOf(project.getName()));

		dao.save(first);
		assertDeepEquals(first, dao.getFirstResultOf(project.getName()));
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void testNoFirstResultFound() {
		checkKalibroException(new Task() {

			@Override
			public void perform() {
				dao.getFirstResultOf(project.getName());
			}
		}, "No project result found", NoResultException.class);
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void testLastResult() {
		dao.save(first);
		assertDeepEquals(first, dao.getLastResultOf(project.getName()));

		dao.save(second);
		assertDeepEquals(second, dao.getLastResultOf(project.getName()));

		dao.save(third);
		assertDeepEquals(third, dao.getLastResultOf(project.getName()));
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void testNoLastResultFound() {
		checkKalibroException(new Task() {

			@Override
			public void perform() {
				dao.getLastResultOf(project.getName());
			}
		}, "No project result found", NoResultException.class);
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void testLastResultBefore() {
		saveResults();
		assertDeepEquals(second, dao.getLastResultBefore(third.getDate(), project.getName()));
		assertDeepEquals(first, dao.getLastResultBefore(second.getDate(), project.getName()));
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void testNoResultFoundBefore() {
		checkKalibroException(new Task() {

			@Override
			public void perform() {
				dao.getLastResultBefore(first.getDate(), project.getName());
			}
		}, "No project result found", NoResultException.class);
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void testFirstResultAfter() {
		saveResults();
		assertDeepEquals(third, dao.getFirstResultAfter(second.getDate(), project.getName()));
		assertDeepEquals(second, dao.getFirstResultAfter(first.getDate(), project.getName()));
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void testNoResultFoundAfter() {
		checkKalibroException(new Task() {

			@Override
			public void perform() {
				dao.getFirstResultAfter(third.getDate(), project.getName());
			}
		}, "No project result found", NoResultException.class);
	}

	private void saveResults() {
		dao.save(first);
		dao.save(second);
		dao.save(third);
	}
}