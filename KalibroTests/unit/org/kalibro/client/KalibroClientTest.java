package org.kalibro.client;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.KalibroSettings;
import org.kalibro.TestCase;
import org.kalibro.core.model.enums.RepositoryType;
import org.kalibro.service.KalibroEndpoint;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

@RunWith(PowerMockRunner.class)
@PrepareForTest({KalibroClient.class, EndpointClient.class, KalibroSettings.class})
public class KalibroClientTest extends TestCase {

	private static final String PROJECT_NAME = "KalibroClientTest project";

	private KalibroClient client;
	private KalibroEndpoint port;

	@Before
	public void setUp() {
		mockSettings();
		createSupressedClient();
	}

	private void mockSettings() {
		mockStatic(KalibroSettings.class);
		when(KalibroSettings.load()).thenReturn(new KalibroSettings());
	}

	private void createSupressedClient() {
		suppress(constructor(EndpointClient.class, String.class, Class.class));
		client = new KalibroClient();

		port = mock(KalibroEndpoint.class);
		Whitebox.setInternalState(client, "port", port);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testSupportedRepositoryTypes() {
		Set<RepositoryType> repositoryTypes = new HashSet<RepositoryType>();
		when(port.getSupportedRepositoryTypes()).thenReturn(repositoryTypes);
		assertSame(repositoryTypes, client.getSupportedRepositoryTypes());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testProcessProject() {
		client.processProject(PROJECT_NAME);
		verify(port).processProject(PROJECT_NAME);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testProcessPeriodically() {
		client.processPeriodically(PROJECT_NAME, 42);
		verify(port).processPeriodically(PROJECT_NAME, 42);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testProcessPeriod() {
		when(port.getProcessPeriod(PROJECT_NAME)).thenReturn(42);
		assertEquals(42, client.getProcessPeriod(PROJECT_NAME).intValue());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testCancelPeriodicProcess() {
		client.cancelPeriodicProcess(PROJECT_NAME);
		verify(port).cancelPeriodicProcess(PROJECT_NAME);
	}
}