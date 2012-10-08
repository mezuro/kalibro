package org.kalibro.service;

import static org.junit.Assert.assertEquals;
import static org.kalibro.ProjectResultFixtures.helloWorldResult;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.Processing;
import org.kalibro.dao.DaoFactory;
import org.kalibro.dao.ProcessingDao;
import org.kalibro.tests.UnitTest;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DaoFactory.class)
public class ProjectResultEndpointImplTest extends UnitTest {

	private boolean flag;
	private ProcessingDao dao;
	private Processing processing;
	private ProjectResultEndpointImpl endpoint;

	@Before
	public void setUp() {
		mockDao();
		flag = new Random(System.currentTimeMillis()).nextBoolean();
		processing = helloWorldResult();
		endpoint = new ProjectResultEndpointImpl();
	}

	private void mockDao() {
		dao = PowerMockito.mock(ProcessingDao.class);
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
		PowerMockito.when(dao.getFirstResultOf("")).thenReturn(processing);
		assertDeepEquals(processing, endpoint.getFirstResultOf("").convert());
	}

	@Test
	public void testGetLastResultOf() {
		PowerMockito.when(dao.getLastResultOf("")).thenReturn(processing);
		assertDeepEquals(processing, endpoint.getLastResultOf("").convert());
	}

	@Test
	public void testGetLastResultBefore() {
		PowerMockito.when(dao.getLastResultBefore(null, "")).thenReturn(processing);
		assertDeepEquals(processing, endpoint.getLastResultBefore(null, "").convert());
	}

	@Test
	public void testGetFirstResultAfter() {
		PowerMockito.when(dao.getFirstResultAfter(null, "")).thenReturn(processing);
		assertDeepEquals(processing, endpoint.getFirstResultAfter(null, "").convert());
	}
}