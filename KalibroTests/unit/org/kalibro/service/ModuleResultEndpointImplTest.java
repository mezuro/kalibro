package org.kalibro.service;

import static org.junit.Assert.*;
import static org.kalibro.core.model.ModuleResultFixtures.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.Kalibro;
import org.kalibro.core.model.ModuleResult;
import org.kalibro.core.persistence.dao.ModuleResultDao;
import org.kalibro.service.entities.ModuleResultXml;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Kalibro.class)
public class ModuleResultEndpointImplTest extends KalibroTestCase {

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
		PowerMockito.mockStatic(Kalibro.class);
		PowerMockito.when(Kalibro.getModuleResultDao()).thenReturn(dao);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testGetModuleResult() {
		PowerMockito.when(dao.getModuleResult("1", "2", new Date(3))).thenReturn(moduleResult);
		assertDeepEquals(moduleResult, endpoint.getModuleResult("1", "2", new Date(3)).convert());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testResultHistory() {
		List<ModuleResult> resultHistory = Arrays.asList(moduleResult);
		PowerMockito.when(dao.getResultHistory("4", "2")).thenReturn(resultHistory);

		List<ModuleResultXml> resultHistoryXml = endpoint.getResultHistory("4", "2");
		assertEquals(1, resultHistoryXml.size());
		assertDeepEquals(moduleResult, resultHistoryXml.get(0).convert());
	}
}