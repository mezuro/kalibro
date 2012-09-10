package org.kalibro.client;

import static org.junit.Assert.assertSame;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.TestCase;
import org.kalibro.core.model.ModuleResult;
import org.kalibro.service.ModuleResultEndpoint;
import org.kalibro.service.entities.ModuleResultXml;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ModuleResultClientDao.class, EndpointClient.class})
public class ModuleResultPortDaoTest extends TestCase {

	private ModuleResult moduleResult;
	private ModuleResultXml moduleResultXml;

	private ModuleResultClientDao dao;
	private ModuleResultEndpoint port;

	@Before
	public void setUp() throws Exception {
		mockModuleResult();
		createSupressedDao();
	}

	private void mockModuleResult() throws Exception {
		moduleResult = mock(ModuleResult.class);
		moduleResultXml = mock(ModuleResultXml.class);
		whenNew(ModuleResultXml.class).withArguments(moduleResult).thenReturn(moduleResultXml);
		when(moduleResultXml.convert()).thenReturn(moduleResult);
	}

	private void createSupressedDao() {
		suppress(constructor(EndpointClient.class, String.class, Class.class));
		dao = new ModuleResultClientDao("");

		port = mock(ModuleResultEndpoint.class);
		Whitebox.setInternalState(dao, "port", port);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testGetModuleResult() {
		when(port.getModuleResult("", "", null)).thenReturn(moduleResultXml);
		assertSame(moduleResult, dao.getModuleResult("", "", null));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testResultHistory() {
		PowerMockito.when(port.getResultHistory("", "")).thenReturn(Arrays.asList(moduleResultXml));
		assertDeepList(dao.getResultHistory("", ""), moduleResult);
	}
}