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
		client.processProject("My project");
		Mockito.verify(port).processProject("My project");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testProcessPeriodically() {
		client.processPeriodically("My project", 42);
		Mockito.verify(port).processPeriodically("My project", 42);
	}
}