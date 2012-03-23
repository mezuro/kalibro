package org.kalibro.client.dao;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.KalibroTestCase;
import org.kalibro.client.EndpointPortFactory;
import org.kalibro.core.model.ModuleResult;
import org.kalibro.core.model.ModuleResultFixtures;
import org.kalibro.service.ModuleResultEndpoint;
import org.kalibro.service.entities.ModuleResultXml;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(EndpointPortFactory.class)
public class ModuleResultPortDaoTest extends KalibroTestCase {

	private ModuleResult moduleResult;

	private ModuleResultPortDao dao;
	private ModuleResultEndpoint port;

	@Before
	public void setUp() {
		mockPort();
		dao = new ModuleResultPortDao();
		moduleResult = ModuleResultFixtures.helloWorldClassResult();
	}

	private void mockPort() {
		port = PowerMockito.mock(ModuleResultEndpoint.class);
		PowerMockito.mockStatic(EndpointPortFactory.class);
		PowerMockito.when(EndpointPortFactory.getEndpointPort(ModuleResultEndpoint.class)).thenReturn(port);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotSaveResultRemotely() {
		checkKalibroException(new Runnable() {

			@Override
			public void run() {
				dao.save(null, null);
			}
		}, "Cannot save module result remotely");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testGetModuleResult() {
		PowerMockito.when(port.getModuleResult("", "", null)).thenReturn(new ModuleResultXml(moduleResult));
		assertDeepEquals(moduleResult, dao.getModuleResult("", "", null));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testResultHistory() {
		List<ModuleResultXml> resultHistory = Arrays.asList(new ModuleResultXml(moduleResult));
		PowerMockito.when(port.getResultHistory("", "")).thenReturn(resultHistory);
		assertDeepEquals(dao.getResultHistory("", ""), moduleResult);
	}
}