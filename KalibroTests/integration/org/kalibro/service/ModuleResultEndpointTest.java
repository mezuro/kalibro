package org.kalibro.service;

import static org.junit.Assert.*;
import static org.kalibro.core.model.ModuleResultFixtures.*;

import java.net.MalformedURLException;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.core.model.ModuleResult;
import org.kalibro.core.persistence.dao.ModuleResultDaoStub;
import org.kalibro.service.entities.ModuleResultXml;

public class ModuleResultEndpointTest extends KalibroServiceTestCase {

	private static final String PROJECT_NAME = "HelloWorld-1.0";
	private static final String CLASS_NAME = "HelloWorld";
	private static final Date DATE_1 = new Date(1);
	private static final Date DATE_2 = new Date(2);

	private ModuleResult applicationResult1, applicationResult2, classResult1, classResult2;

	private ModuleResultEndpoint port;

	@Before
	public void setUp() throws MalformedURLException {
		applicationResult1 = helloWorldApplicationResult(DATE_1);
		applicationResult2 = helloWorldApplicationResult(DATE_2);
		classResult1 = helloWorldClassResult(DATE_1);
		classResult2 = helloWorldClassResult(DATE_2);
		ModuleResultDaoStub daoStub = new ModuleResultDaoStub();
		daoStub.save(classResult1, PROJECT_NAME, DATE_1);
		daoStub.save(classResult2, PROJECT_NAME, DATE_2);
		daoStub.save(applicationResult1, PROJECT_NAME, DATE_1);
		daoStub.save(applicationResult2, PROJECT_NAME, DATE_2);
		port = publishAndGetPort(new ModuleResultEndpointImpl(daoStub), ModuleResultEndpoint.class);
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void shouldRetrieveModuleResult() {
		assertDeepEquals(applicationResult1, port.getModuleResult(PROJECT_NAME, PROJECT_NAME, DATE_1).convert());
		assertDeepEquals(applicationResult2, port.getModuleResult(PROJECT_NAME, PROJECT_NAME, DATE_2).convert());
		assertDeepEquals(classResult1, port.getModuleResult(PROJECT_NAME, CLASS_NAME, DATE_1).convert());
		assertDeepEquals(classResult2, port.getModuleResult(PROJECT_NAME, CLASS_NAME, DATE_2).convert());
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void shouldRetrieveResultHistory() {
		List<ModuleResultXml> applicationHistory = port.getResultHistory(PROJECT_NAME, PROJECT_NAME);
		assertEquals(2, applicationHistory.size());
		assertDeepEquals(applicationResult1, applicationHistory.get(0).convert());
		assertDeepEquals(applicationResult2, applicationHistory.get(1).convert());

		List<ModuleResultXml> classHistory = port.getResultHistory(PROJECT_NAME, CLASS_NAME);
		assertEquals(2, classHistory.size());
		assertDeepEquals(classResult1, classHistory.get(0).convert());
		assertDeepEquals(classResult2, classHistory.get(1).convert());
	}
}