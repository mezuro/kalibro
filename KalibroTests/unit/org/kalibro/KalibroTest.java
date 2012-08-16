package org.kalibro;

import static org.junit.Assert.*;
import static org.mockito.internal.verification.VerificationModeFactory.*;
import static org.powermock.api.mockito.PowerMockito.*;

import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.client.KalibroClient;
import org.kalibro.core.KalibroLocal;
import org.kalibro.core.concurrent.Task;
import org.kalibro.core.model.enums.RepositoryType;
import org.kalibro.core.persistence.dao.*;
import org.kalibro.core.settings.KalibroSettings;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Kalibro.class, KalibroSettings.class})
public class KalibroTest extends KalibroTestCase {

	private static final String PROJECT_NAME = "KalibroTest project";

	@BeforeClass
	public static void emmaCoverage() throws Exception {
		Kalibro.class.getDeclaredConstructor().newInstance();
	}

	private KalibroSettings settings;
	private KalibroFacade facade;
	private DaoFactory daoFactory;

	@Before
	public void setUp() throws Exception {
		mockSettings();
		mockFacade();
	}

	private void mockSettings() {
		settings = mock(KalibroSettings.class);
		mockStatic(KalibroSettings.class);
		when(KalibroSettings.load()).thenReturn(settings);
		when(settings.isClient()).thenReturn(false);
	}

	private void mockFacade() throws Exception {
		facade = mock(KalibroLocal.class);
		daoFactory = mock(DaoFactory.class);
		when(facade.getDaoFactory()).thenReturn(daoFactory);
		whenNew(KalibroLocal.class).withNoArguments().thenReturn((KalibroLocal) facade);
		whenNew(KalibroClient.class).withNoArguments().thenReturn(mock(KalibroClient.class));
	}

	@After
	public void tearDown() {
		Whitebox.setInternalState(Kalibro.class, "facade", (Object) null);
		Whitebox.setInternalState(Kalibro.class, "settings", (Object) null);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testSettingsFileExists() {
		when(KalibroSettings.settingsFileExists()).thenReturn(true);
		assertTrue(Kalibro.settingsFileExists());

		when(KalibroSettings.settingsFileExists()).thenReturn(false);
		assertFalse(Kalibro.settingsFileExists());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testCurrentSettings() {
		assertSame(settings, Kalibro.currentSettings());
		assertSame(settings, Kalibro.currentSettings());
		verifyStatic(times(1));
		KalibroSettings.load();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testChangeSettingsToLocal() {
		testChangeSettings(false, KalibroLocal.class);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testChangeSettingsToClient() {
		testChangeSettings(true, KalibroClient.class);
	}

	private void testChangeSettings(boolean asClient, Class<? extends KalibroFacade> facadeClass) {
		when(settings.isClient()).thenReturn(asClient);
		Kalibro.changeSettings(settings);

		assertSame(settings, Kalibro.currentSettings());
		Mockito.verify(settings).save();
		verifyNew(facadeClass);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotWriteNewSettingsIfFacadeCreationThrowsException() throws Exception {
		when(settings.isClient()).thenReturn(false);
		whenNew(KalibroLocal.class).withNoArguments().thenThrow(new KalibroException("KalibroTest"));

		checkKalibroException(new Task() {

			@Override
			public void perform() throws Exception {
				Kalibro.changeSettings(settings);
			}
		}, "KalibroTest");
		Mockito.verify(settings, times(0)).save();

		Kalibro.currentSettings();
		verifyStatic();
		KalibroSettings.load();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testBaseToolDao() {
		BaseToolDao baseToolDao = mock(BaseToolDao.class);
		when(daoFactory.getBaseToolDao()).thenReturn(baseToolDao);
		assertSame(baseToolDao, Kalibro.getBaseToolDao());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testConfigurationDao() {
		ConfigurationDao configurationDao = mock(ConfigurationDao.class);
		when(daoFactory.getConfigurationDao()).thenReturn(configurationDao);
		assertSame(configurationDao, Kalibro.getConfigurationDao());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testMetricConfigurationDao() {
		MetricConfigurationDao metricConfigurationDao = mock(MetricConfigurationDao.class);
		when(daoFactory.getMetricConfigurationDao()).thenReturn(metricConfigurationDao);
		assertSame(metricConfigurationDao, Kalibro.getMetricConfigurationDao());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testProjectDao() {
		ProjectDao projectDao = mock(ProjectDao.class);
		when(daoFactory.getProjectDao()).thenReturn(projectDao);
		assertSame(projectDao, Kalibro.getProjectDao());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testProjectResultDao() {
		ProjectResultDao projectResultDao = mock(ProjectResultDao.class);
		when(daoFactory.getProjectResultDao()).thenReturn(projectResultDao);
		assertSame(projectResultDao, Kalibro.getProjectResultDao());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testModuleResultDao() {
		ModuleResultDao moduleResultDao = mock(ModuleResultDao.class);
		when(daoFactory.getModuleResultDao()).thenReturn(moduleResultDao);
		assertSame(moduleResultDao, Kalibro.getModuleResultDao());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testSupportedRepositoryTypes() {
		Set<RepositoryType> repositoryTypes = mock(Set.class);
		when(facade.getSupportedRepositoryTypes()).thenReturn(repositoryTypes);
		assertSame(repositoryTypes, Kalibro.getSupportedRepositoryTypes());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testProcessProject() {
		Kalibro.processProject(PROJECT_NAME);
		Mockito.verify(facade).processProject(PROJECT_NAME);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testProcessPeriodically() {
		Kalibro.processPeriodically(PROJECT_NAME, 42);
		Mockito.verify(facade).processPeriodically(PROJECT_NAME, 42);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testProcessPeriod() {
		when(facade.getProcessPeriod(PROJECT_NAME)).thenReturn(42);
		assertEquals(42, Kalibro.getProcessPeriod(PROJECT_NAME).intValue());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testCancelPeriodicProcess() {
		Kalibro.cancelPeriodicProcess(PROJECT_NAME);
		Mockito.verify(facade).cancelPeriodicProcess(PROJECT_NAME);
	}
}