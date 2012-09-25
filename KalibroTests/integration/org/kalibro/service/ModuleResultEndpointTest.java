package org.kalibro.service;

import static org.kalibro.ConfigurationFixtures.newConfiguration;
import static org.kalibro.ModuleResultFixtures.newHelloWorldClassResult;

import java.util.Arrays;
import java.util.Date;

import org.junit.Test;
import org.kalibro.ModuleResult;
import org.kalibro.client.EndpointTest;
import org.kalibro.dao.ModuleResultDao;

public class ModuleResultEndpointTest extends EndpointTest<ModuleResult, ModuleResultDao, ModuleResultEndpoint> {

	private static final String PROJECT_NAME = "ModuleResultEndpointTest project name";
	private static final String MODULE_NAME = "ModuleResultEndpointTest module name";
	private static final Date DATE = new Date();

	@Override
	protected ModuleResult loadFixture() {
		ModuleResult fixture = newHelloWorldClassResult(DATE);
		fixture.setConfiguration(newConfiguration("cbo", "lcom4"));
		return fixture;
	}

	@Test
	public void shouldRetrieveModuleResult() {
		when(dao.getModuleResult(PROJECT_NAME, MODULE_NAME, DATE)).thenReturn(entity);
		assertDeepDtoEquals(entity, port.getModuleResult(PROJECT_NAME, MODULE_NAME, DATE));
	}

	@Test
	public void shouldRetrieveResultHistory() {
		when(dao.getResultHistory(PROJECT_NAME, MODULE_NAME)).thenReturn(Arrays.asList(entity));
		assertDeepDtoList(port.getResultHistory(PROJECT_NAME, MODULE_NAME), entity);
	}
}