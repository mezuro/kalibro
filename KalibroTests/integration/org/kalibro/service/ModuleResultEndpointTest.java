package org.kalibro.service;

import static org.junit.Assert.*;
import static org.kalibro.core.model.ConfigurationFixtures.*;
import static org.kalibro.core.model.ModuleResultFixtures.*;

import java.net.MalformedURLException;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.core.model.ModuleResult;
import org.kalibro.dao.ModuleResultDaoFake;
import org.kalibro.service.entities.ModuleResultXml;

public class ModuleResultEndpointTest extends EndpointTest {

	private static final String PROJECT_NAME = "HelloWorld-1.0";
	private static final String CLASS_NAME = "HelloWorld";
	private static final Date DATE_1 = new Date(1);
	private static final Date DATE_2 = new Date(2);

	private ModuleResultDaoFake daoFake;
	private ModuleResultEndpoint port;

	@Before
	public void setUp() throws MalformedURLException {
		daoFake = new ModuleResultDaoFake();
		configureAndAddModuleResult(newHelloWorldApplicationResult(DATE_1));
		configureAndAddModuleResult(newHelloWorldApplicationResult(DATE_2));
		configureAndAddModuleResult(newHelloWorldClassResult(DATE_1));
		configureAndAddModuleResult(newHelloWorldClassResult(DATE_2));
		port = publishAndGetPort(new ModuleResultEndpointImpl(daoFake), ModuleResultEndpoint.class);
	}

	private void configureAndAddModuleResult(ModuleResult moduleResult) {
		moduleResult.setConfiguration(newConfiguration("cbo", "lcom4"));
		daoFake.save(moduleResult, PROJECT_NAME);
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void shouldRetrieveModuleResult() {
		verifyModuleResult(PROJECT_NAME, DATE_1);
		verifyModuleResult(PROJECT_NAME, DATE_1);
		verifyModuleResult(PROJECT_NAME, DATE_2);
		verifyModuleResult(CLASS_NAME, DATE_1);
		verifyModuleResult(CLASS_NAME, DATE_2);
	}

	private void verifyModuleResult(String moduleName, Date date) {
		ModuleResult expected = daoFake.getModuleResult(PROJECT_NAME, moduleName, date);
		ModuleResult actual = port.getModuleResult(PROJECT_NAME, moduleName, date).convert();
		assertNotSame(expected, actual);
		assertDeepEquals(expected, actual);
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void shouldRetrieveResultHistory() {
		verifyResultHistory(PROJECT_NAME);
		verifyResultHistory(CLASS_NAME);
	}

	private void verifyResultHistory(String moduleName) {
		List<ModuleResult> expected = daoFake.getResultHistory(PROJECT_NAME, moduleName);
		List<ModuleResultXml> actual = port.getResultHistory(PROJECT_NAME, moduleName);
		assertEquals(expected.size(), actual.size());
		for (int i = 0; i < expected.size(); i++)
			assertDeepEquals(expected.get(i), actual.get(i).convert());
	}
}