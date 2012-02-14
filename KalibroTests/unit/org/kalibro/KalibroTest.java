package org.kalibro;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.client.KalibroClient;
import org.kalibro.core.KalibroLocal;
import org.kalibro.core.ProjectStateListener;
import org.kalibro.core.model.Project;
import org.kalibro.core.model.enums.RepositoryType;
import org.kalibro.core.persistence.dao.*;
import org.kalibro.core.settings.KalibroSettings;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Kalibro.class, KalibroSettings.class})
public class KalibroTest extends KalibroTestCase {

	@BeforeClass
	public static void emmaCoverage() {
		new Kalibro();
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
		settings = PowerMockito.mock(KalibroSettings.class);
		PowerMockito.mockStatic(KalibroSettings.class);
		PowerMockito.when(KalibroSettings.load()).thenReturn(settings);
		PowerMockito.when(settings.isClient()).thenReturn(false);
	}

	private void mockFacade() throws Exception {
		facade = PowerMockito.mock(KalibroLocal.class);
		daoFactory = PowerMockito.mock(DaoFactory.class);
		PowerMockito.when(facade.getDaoFactory()).thenReturn(daoFactory);
		PowerMockito.whenNew(KalibroLocal.class).withNoArguments().thenReturn((KalibroLocal) facade);
		PowerMockito.whenNew(KalibroClient.class).withNoArguments().thenReturn(PowerMockito.mock(KalibroClient.class));
	}

	@After
	public void tearDown() {
		Whitebox.setInternalState(Kalibro.class, "facade", (Object) null);
		Whitebox.setInternalState(Kalibro.class, "settings", (Object) null);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testSettingsFileExists() {
		PowerMockito.when(KalibroSettings.settingsFileExists()).thenReturn(true);
		assertTrue(Kalibro.settingsFileExists());

		PowerMockito.when(KalibroSettings.settingsFileExists()).thenReturn(false);
		assertFalse(Kalibro.settingsFileExists());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testCurrentSettings() {
		assertSame(settings, Kalibro.currentSettings());
		assertSame(settings, Kalibro.currentSettings());
		PowerMockito.verifyStatic(Mockito.times(1));
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
		PowerMockito.when(settings.isClient()).thenReturn(asClient);
		Kalibro.changeSettings(settings);

		assertSame(settings, Kalibro.currentSettings());
		Mockito.verify(settings).write();
		PowerMockito.verifyNew(facadeClass);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testBaseToolDao() {
		BaseToolDao baseToolDao = PowerMockito.mock(BaseToolDao.class);
		PowerMockito.when(daoFactory.getBaseToolDao()).thenReturn(baseToolDao);
		assertSame(baseToolDao, Kalibro.getBaseToolDao());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testConfigurationDao() {
		ConfigurationDao configurationDao = PowerMockito.mock(ConfigurationDao.class);
		PowerMockito.when(daoFactory.getConfigurationDao()).thenReturn(configurationDao);
		assertSame(configurationDao, Kalibro.getConfigurationDao());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testProjectDao() {
		ProjectDao projectDao = PowerMockito.mock(ProjectDao.class);
		PowerMockito.when(daoFactory.getProjectDao()).thenReturn(projectDao);
		assertSame(projectDao, Kalibro.getProjectDao());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testProjectResultDao() {
		ProjectResultDao projectResultDao = PowerMockito.mock(ProjectResultDao.class);
		PowerMockito.when(daoFactory.getProjectResultDao()).thenReturn(projectResultDao);
		assertSame(projectResultDao, Kalibro.getProjectResultDao());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testModuleResultDao() {
		ModuleResultDao moduleResultDao = PowerMockito.mock(ModuleResultDao.class);
		PowerMockito.when(daoFactory.getModuleResultDao()).thenReturn(moduleResultDao);
		assertSame(moduleResultDao, Kalibro.getModuleResultDao());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testSupportedRepositoryTypes() {
		Set<RepositoryType> repositoryTypes = PowerMockito.mock(Set.class);
		PowerMockito.when(facade.getSupportedRepositoryTypes()).thenReturn(repositoryTypes);
		assertSame(repositoryTypes, Kalibro.getSupportedRepositoryTypes());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testProcessProject() {
		String projectName = "My project";
		Kalibro.processProject(projectName);
		Mockito.verify(facade).processProject(projectName);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testProcessPeriodically() {
		Kalibro.processPeriodically("My project", 42);
		Mockito.verify(facade).processPeriodically("My project", 42);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testAddProjectStateListener() {
		Project project = PowerMockito.mock(Project.class);
		ProjectStateListener listener = PowerMockito.mock(ProjectStateListener.class);
		Kalibro.addProjectStateListener(project, listener);
		Mockito.verify(facade).addProjectStateListener(project, listener);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testRemoveProjectStateListener() {
		ProjectStateListener listener = PowerMockito.mock(ProjectStateListener.class);
		Kalibro.removeProjectStateListener(listener);
		Mockito.verify(facade).removeProjectStateListener(listener);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testFireProjectStateChanged() {
		Project project = PowerMockito.mock(Project.class);
		Kalibro.fireProjectStateChanged(project);
		Mockito.verify(facade).fireProjectStateChanged(project);
	}
}