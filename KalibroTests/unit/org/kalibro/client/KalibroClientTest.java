package org.kalibro.client;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.Kalibro;
import org.kalibro.KalibroTestCase;
import org.kalibro.client.dao.PortDaoFactory;
import org.kalibro.core.model.enums.RepositoryType;
import org.kalibro.core.settings.KalibroSettings;
import org.kalibro.service.KalibroEndpoint;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({EndpointPortFactory.class, Kalibro.class, KalibroClient.class})
public class KalibroClientTest extends KalibroTestCase {

	private static final String PROJECT_NAME = "KalibroClientTest project";

	private KalibroClient client;
	private KalibroEndpoint port;
	private KalibroSettings settings;
	private ProjectStateTracker tracker;

	@Before
	public void setUp() throws Exception {
		mockPort();
		mockSettings();
		mockTracker();
		client = new KalibroClient();
	}

	private void mockPort() {
		port = PowerMockito.mock(KalibroEndpoint.class);
		PowerMockito.mockStatic(EndpointPortFactory.class);
		PowerMockito.when(EndpointPortFactory.getEndpointPort(KalibroEndpoint.class)).thenReturn(port);
	}

	private void mockSettings() {
		settings = new KalibroSettings();
		PowerMockito.mockStatic(Kalibro.class);
		PowerMockito.when(Kalibro.currentSettings()).thenReturn(settings);
	}

	private void mockTracker() throws Exception {
		tracker = PowerMockito.mock(ProjectStateTracker.class);
		PowerMockito.whenNew(ProjectStateTracker.class).withArguments(any(), any()).thenReturn(tracker);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldStartPeriodicExecutionOnInitialization() {
		Mockito.verify(tracker).executePeriodically(settings.getPollingInterval());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldStopPeriodicExecutionOnFinalization() throws Throwable {
		client.finalize();
		Mockito.verify(tracker).cancelPeriodicExecution();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkDaoFactory() {
		assertClassEquals(PortDaoFactory.class, client.createDaoFactory());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testSupportedRepositoryTypes() {
		Set<RepositoryType> repositoryTypes = new HashSet<RepositoryType>();
		PowerMockito.when(port.getSupportedRepositoryTypes()).thenReturn(repositoryTypes);
		assertSame(repositoryTypes, client.getSupportedRepositoryTypes());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testProcessProject() {
		client.processProject(PROJECT_NAME);
		Mockito.verify(port).processProject(PROJECT_NAME);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testProcessPeriodically() {
		client.processPeriodically(PROJECT_NAME, 42);
		Mockito.verify(port).processPeriodically(PROJECT_NAME, 42);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testProcessPeriod() {
		PowerMockito.when(port.getProcessPeriod(PROJECT_NAME)).thenReturn(42);
		assertEquals(42, client.getProcessPeriod(PROJECT_NAME).intValue());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testCancelPeriodicProcess() {
		client.cancelPeriodicProcess(PROJECT_NAME);
		Mockito.verify(port).cancelPeriodicProcess(PROJECT_NAME);
	}
}