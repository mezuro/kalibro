package org.kalibro.service;

import static org.junit.Assert.assertEquals;
import static org.kalibro.core.model.enums.RepositoryType.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.Kalibro;
import org.kalibro.core.model.enums.RepositoryType;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Kalibro.class)
public class KalibroEndpointImplTest extends KalibroTestCase {

	private static final String PROJECT_NAME = "KalibroEndpointImplTest project";

	private KalibroEndpointImpl endpoint;

	@Before
	public void setUp() {
		PowerMockito.mockStatic(Kalibro.class);
		endpoint = new KalibroEndpointImpl();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testSupportedRepositoryTypes() {
		Set<RepositoryType> repositoryTypes = new HashSet<RepositoryType>();
		repositoryTypes.addAll(Arrays.asList(LOCAL_ZIP, GIT, SUBVERSION));
		PowerMockito.when(Kalibro.getSupportedRepositoryTypes()).thenReturn(repositoryTypes);
		assertDeepSet(endpoint.getSupportedRepositoryTypes(), GIT, SUBVERSION);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testProcessProject() {
		endpoint.processProject(PROJECT_NAME);
		PowerMockito.verifyStatic();
		Kalibro.processProject(PROJECT_NAME);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testProcessPeriodically() {
		endpoint.processPeriodically(PROJECT_NAME, 42);
		PowerMockito.verifyStatic();
		Kalibro.processPeriodically(PROJECT_NAME, 42);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testProcessPeriod() {
		PowerMockito.when(Kalibro.getProcessPeriod(PROJECT_NAME)).thenReturn(42);
		assertEquals(42, endpoint.getProcessPeriod(PROJECT_NAME).intValue());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testCancelPeriodicProcess() {
		endpoint.cancelPeriodicProcess(PROJECT_NAME);
		PowerMockito.verifyStatic();
		Kalibro.cancelPeriodicProcess(PROJECT_NAME);
	}
}