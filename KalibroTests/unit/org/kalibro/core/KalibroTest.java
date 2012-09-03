package org.kalibro.core;

import static org.junit.Assert.*;
import static org.powermock.api.mockito.PowerMockito.*;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.KalibroSettings;
import org.kalibro.UtilityClassTest;
import org.kalibro.client.KalibroClient;
import org.kalibro.core.model.enums.RepositoryType;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Kalibro.class, KalibroSettings.class})
public class KalibroTest extends UtilityClassTest {

	private static final String PROJECT_NAME = "KalibroTest project";

	private KalibroSettings settings;
	private KalibroLocal localFacade;
	private KalibroClient clientFacade;

	@Before
	public void setUp() throws Exception {
		mockSettings();
		mockFacade();
	}

	private void mockSettings() {
		settings = mock(KalibroSettings.class);
		mockStatic(KalibroSettings.class);
		when(KalibroSettings.load()).thenReturn(settings);
	}

	private void mockFacade() throws Exception {
		localFacade = mock(KalibroLocal.class);
		clientFacade = mock(KalibroClient.class);
		whenNew(KalibroLocal.class).withNoArguments().thenReturn(localFacade);
		whenNew(KalibroClient.class).withNoArguments().thenReturn(clientFacade);
	}

	@Override
	protected Class<?> utilityClass() {
		return Kalibro.class;
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldCreateFacadeAccordingToServiceSide() throws Exception {
		verifyServiceSideFacade(false);
		verifyServiceSideFacade(true);
	}

	private void verifyServiceSideFacade(boolean clientSide) throws Exception {
		when(settings.clientSide()).thenReturn(clientSide);
		Kalibro.cancelPeriodicProcess(PROJECT_NAME);

		Class<?> facadeClass = clientSide ? KalibroClient.class : KalibroLocal.class;
		verifyNew(facadeClass).withNoArguments();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldGetSupportedRepositoryTypes() {
		Set<RepositoryType> repositoryTypes = mock(Set.class);
		when(localFacade.getSupportedRepositoryTypes()).thenReturn(repositoryTypes);
		assertSame(repositoryTypes, Kalibro.getSupportedRepositoryTypes());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldProcessProject() {
		Kalibro.processProject(PROJECT_NAME);
		Mockito.verify(localFacade).processProject(PROJECT_NAME);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldProcessPeriodically() {
		Kalibro.processPeriodically(PROJECT_NAME, 42);
		Mockito.verify(localFacade).processPeriodically(PROJECT_NAME, 42);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldGetProcessPeriod() {
		when(localFacade.getProcessPeriod(PROJECT_NAME)).thenReturn(42);
		assertEquals(42, Kalibro.getProcessPeriod(PROJECT_NAME).intValue());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldCancelPeriodicProcess() {
		Kalibro.cancelPeriodicProcess(PROJECT_NAME);
		Mockito.verify(localFacade).cancelPeriodicProcess(PROJECT_NAME);
	}
}