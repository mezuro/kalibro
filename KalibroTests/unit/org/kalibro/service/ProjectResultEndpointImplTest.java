package org.kalibro.service;

import static org.junit.Assert.assertEquals;
import static org.kalibro.core.model.ProjectResultFixtures.helloWorldResult;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.TestCase;
import org.kalibro.core.model.ProjectResult;
import org.kalibro.core.persistence.dao.DaoFactory;
import org.kalibro.core.persistence.dao.ProjectResultDao;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DaoFactory.class)
public class ProjectResultEndpointImplTest extends TestCase {

	private boolean flag;
	private ProjectResultDao dao;
	private ProjectResult projectResult;
	private ProjectResultEndpointImpl endpoint;

	@Before
	public void setUp() {
		mockDao();
		flag = new Random(System.currentTimeMillis()).nextBoolean();
		projectResult = helloWorldResult();
		endpoint = new ProjectResultEndpointImpl();
	}

	private void mockDao() {
		dao = PowerMockito.mock(ProjectResultDao.class);
		PowerMockito.mockStatic(DaoFactory.class);
		PowerMockito.when(DaoFactory.getProjectResultDao()).thenReturn(dao);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testHasResultsFor() {
		PowerMockito.when(dao.hasResultsFor("")).thenReturn(flag);
		assertEquals(flag, endpoint.hasResultsFor(""));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testHasResultsBefore() {
		PowerMockito.when(dao.hasResultsBefore(null, "")).thenReturn(flag);
		assertEquals(flag, endpoint.hasResultsBefore(null, ""));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testHasResultsAfter() {
		PowerMockito.when(dao.hasResultsAfter(null, "")).thenReturn(flag);
		assertEquals(flag, endpoint.hasResultsAfter(null, ""));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testGetFirstResultOf() {
		PowerMockito.when(dao.getFirstResultOf("")).thenReturn(projectResult);
		assertDeepEquals(projectResult, endpoint.getFirstResultOf("").convert());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testGetLastResultOf() {
		PowerMockito.when(dao.getLastResultOf("")).thenReturn(projectResult);
		assertDeepEquals(projectResult, endpoint.getLastResultOf("").convert());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testGetLastResultBefore() {
		PowerMockito.when(dao.getLastResultBefore(null, "")).thenReturn(projectResult);
		assertDeepEquals(projectResult, endpoint.getLastResultBefore(null, "").convert());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testGetFirstResultAfter() {
		PowerMockito.when(dao.getFirstResultAfter(null, "")).thenReturn(projectResult);
		assertDeepEquals(projectResult, endpoint.getFirstResultAfter(null, "").convert());
	}
}