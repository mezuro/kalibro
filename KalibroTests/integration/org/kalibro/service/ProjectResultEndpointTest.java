package org.kalibro.service;

import static org.junit.Assert.*;
import static org.kalibro.core.model.ProjectResultFixtures.*;

import java.net.MalformedURLException;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.core.model.ProjectResult;
import org.kalibro.core.persistence.dao.ProjectResultDao;
import org.kalibro.core.persistence.dao.ProjectResultDaoStub;

public class ProjectResultEndpointTest extends KalibroServiceTestCase {

	private static final String PROJECT_NAME = "HelloWorld-1.0";
	private static final Date DATE_1 = new Date(1);
	private static final Date DATE_2 = new Date(2);
	private static final Date DATE_3 = new Date(3);

	private ProjectResultDao daoStub;
	private ProjectResultEndpoint port;

	@Before
	public void setUp() throws MalformedURLException {
		daoStub = new ProjectResultDaoStub();
		daoStub.save(helloWorldResult(DATE_1));
		daoStub.save(helloWorldResult(DATE_2));
		daoStub.save(helloWorldResult(DATE_3));
		port = publishAndGetPort(new ProjectResultEndpointImpl(daoStub), ProjectResultEndpoint.class);
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void testHasResultsFor() {
		assertTrue(port.hasResultsFor(PROJECT_NAME));
		assertFalse(port.hasResultsFor("ProjectResultEndpointTest"));
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void testHasResultsBefore() {
		assertFalse(port.hasResultsBefore(DATE_1, PROJECT_NAME));
		assertTrue(port.hasResultsBefore(DATE_2, PROJECT_NAME));
		assertTrue(port.hasResultsBefore(DATE_3, PROJECT_NAME));
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void testHasResultsAfter() {
		assertTrue(port.hasResultsAfter(DATE_1, PROJECT_NAME));
		assertTrue(port.hasResultsAfter(DATE_2, PROJECT_NAME));
		assertFalse(port.hasResultsAfter(DATE_3, PROJECT_NAME));
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void testFirstResult() {
		verifyResult(daoStub.getFirstResultOf(PROJECT_NAME), port.getFirstResultOf(PROJECT_NAME).convert());
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void testLastResult() {
		verifyResult(daoStub.getLastResultOf(PROJECT_NAME), port.getLastResultOf(PROJECT_NAME).convert());
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void testLastResultBefore() {
		verifyLastResultBefore(DATE_2);
		verifyLastResultBefore(DATE_3);
	}

	private void verifyLastResultBefore(Date date) {
		ProjectResult expected = daoStub.getLastResultBefore(date, PROJECT_NAME);
		ProjectResult actual = port.getLastResultBefore(date, PROJECT_NAME).convert();
		verifyResult(expected, actual);
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void testFirstResultAfter() {
		verifyFirstResultAfter(DATE_1);
		verifyFirstResultAfter(DATE_2);
	}

	private void verifyFirstResultAfter(Date date) {
		ProjectResult expected = daoStub.getFirstResultAfter(date, PROJECT_NAME);
		ProjectResult actual = port.getFirstResultAfter(date, PROJECT_NAME).convert();
		verifyResult(expected, actual);
	}

	private void verifyResult(ProjectResult expected, ProjectResult actual) {
		assertNotSame(expected, actual);
		assertDeepEquals(expected, actual);
	}
}