package org.kalibro.service;

import static org.junit.Assert.assertEquals;
import static org.kalibro.core.model.ModuleResultFixtures.helloWorldApplicationResult;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.core.model.ModuleResult;
import org.kalibro.dao.DaoFactory;
import org.kalibro.dao.ModuleResultDao;
import org.kalibro.service.entities.ModuleResultXml;
import org.kalibro.tests.UnitTest;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DaoFactory.class)
public class ModuleResultEndpointImplTest extends UnitTest {

	private ModuleResultDao dao;
	private ModuleResult moduleResult;
	private ModuleResultEndpointImpl endpoint;

	@Before
	public void setUp() {
		mockDao();
		moduleResult = helloWorldApplicationResult();
		endpoint = new ModuleResultEndpointImpl();
	}

	private void mockDao() {
		dao = PowerMockito.mock(ModuleResultDao.class);
		PowerMockito.mockStatic(DaoFactory.class);
		PowerMockito.when(DaoFactory.getModuleResultDao()).thenReturn(dao);
	}

	@Test
	public void testGetModuleResult() {
		PowerMockito.when(dao.getModuleResult("1", "2", new Date(3))).thenReturn(moduleResult);
		assertDeepEquals(moduleResult, endpoint.getModuleResult("1", "2", new Date(3)).convert());
	}

	@Test
	public void testResultHistory() {
		List<ModuleResult> resultHistory = Arrays.asList(moduleResult);
		PowerMockito.when(dao.getResultHistory("4", "2")).thenReturn(resultHistory);

		List<ModuleResultXml> resultHistoryXml = endpoint.getResultHistory("4", "2");
		assertEquals(1, resultHistoryXml.size());
		assertDeepEquals(moduleResult, resultHistoryXml.get(0).convert());
	}
}