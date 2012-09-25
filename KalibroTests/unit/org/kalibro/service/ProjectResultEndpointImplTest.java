package org.kalibro.service;

import static org.junit.Assert.assertEquals;
import static org.kalibro.core.model.ProjectResultFixtures.helloWorldResult;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.ProjectResult;
import org.kalibro.dao.DaoFactory;
import org.kalibro.dao.ProjectResultDao;
import org.kalibro.tests.UnitTest;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DaoFactory.class)
public class ProjectResultEndpointImplTest extends UnitTest {

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

	@Test
	public void testHasResultsFor() {
		PowerMockito.when(dao.hasResultsFor("")).thenReturn(flag);
		assertEquals(flag, endpoint.hasResultsFor(""));
	}

	@Test
	public void testHasResultsBefore() {
		PowerMockito.when(dao.hasResultsBefore(null, "")).thenReturn(flag);
		assertEquals(flag, endpoint.hasResultsBefore(null, ""));
	}

	@Test
	public void testHasResultsAfter() {
		PowerMockito.when(dao.hasResultsAfter(null, "")).thenReturn(flag);
		assertEquals(flag, endpoint.hasResultsAfter(null, ""));
	}

	@Test
	public void testGetFirstResultOf() {
		PowerMockito.when(dao.getFirstResultOf("")).thenReturn(projectResult);
		assertDeepEquals(projectResult, endpoint.getFirstResultOf("").convert());
	}

	@Test
	public void testGetLastResultOf() {
		PowerMockito.when(dao.getLastResultOf("")).thenReturn(projectResult);
		assertDeepEquals(projectResult, endpoint.getLastResultOf("").convert());
	}

	@Test
	public void testGetLastResultBefore() {
		PowerMockito.when(dao.getLastResultBefore(null, "")).thenReturn(projectResult);
		assertDeepEquals(projectResult, endpoint.getLastResultBefore(null, "").convert());
	}

	@Test
	public void testGetFirstResultAfter() {
		PowerMockito.when(dao.getFirstResultAfter(null, "")).thenReturn(projectResult);
		assertDeepEquals(projectResult, endpoint.getFirstResultAfter(null, "").convert());
	}
}