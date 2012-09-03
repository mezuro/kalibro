package org.kalibro.core;

import static org.junit.Assert.*;
import static org.powermock.api.mockito.PowerMockito.*;

import java.util.Set;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.KalibroSettings;
import org.kalibro.TestCase;
import org.kalibro.client.KalibroClient;
import org.kalibro.core.model.enums.RepositoryType;
import org.kalibro.core.persistence.dao.*;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Kalibro.class, KalibroSettings.class})
public class KalibroTest extends TestCase {

	private static final String PROJECT_NAME = "KalibroTest project";

	@BeforeClass
	public static void emmaCoverage() throws Exception {
		Kalibro.class.getDeclaredConstructor().newInstance();
	}

	private KalibroSettings settings;
	private DaoFactory daoFactory;
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
		daoFactory = mock(DaoFactory.class);
		localFacade = mock(KalibroLocal.class);
		clientFacade = mock(KalibroClient.class);
		when(localFacade.getDaoFactory()).thenReturn(daoFactory);
		when(clientFacade.getDaoFactory()).thenReturn(daoFactory);
		whenNew(KalibroLocal.class).withNoArguments().thenReturn(localFacade);
		whenNew(KalibroClient.class).withNoArguments().thenReturn(clientFacade);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldCreateFacadeAccordingToServiceSide() throws Exception {
		verifyServiceSideFacade(false);
		verifyServiceSideFacade(true);
	}

	private void verifyServiceSideFacade(boolean clientSide) throws Exception {
		when(settings.clientSide()).thenReturn(clientSide);
		Kalibro.getProjectDao();

		Class<?> facadeClass = clientSide ? KalibroClient.class : KalibroLocal.class;
		verifyNew(facadeClass).withNoArguments();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldGetBaseToolDao() {
		BaseToolDao baseToolDao = mock(BaseToolDao.class);
		when(daoFactory.createBaseToolDao()).thenReturn(baseToolDao);
		assertSame(baseToolDao, Kalibro.getBaseToolDao());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldGetConfigurationDao() {
		ConfigurationDao configurationDao = mock(ConfigurationDao.class);
		when(daoFactory.createConfigurationDao()).thenReturn(configurationDao);
		assertSame(configurationDao, Kalibro.getConfigurationDao());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldGetMetricConfigurationDao() {
		MetricConfigurationDao metricConfigurationDao = mock(MetricConfigurationDao.class);
		when(daoFactory.createMetricConfigurationDao()).thenReturn(metricConfigurationDao);
		assertSame(metricConfigurationDao, Kalibro.getMetricConfigurationDao());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testProjectDao() {
		ProjectDao projectDao = mock(ProjectDao.class);
		when(daoFactory.createProjectDao()).thenReturn(projectDao);
		assertSame(projectDao, Kalibro.getProjectDao());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldGetProjectResultDao() {
		ProjectResultDao projectResultDao = mock(ProjectResultDao.class);
		when(daoFactory.createProjectResultDao()).thenReturn(projectResultDao);
		assertSame(projectResultDao, Kalibro.getProjectResultDao());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldGetModuleResultDao() {
		ModuleResultDao moduleResultDao = mock(ModuleResultDao.class);
		when(daoFactory.createModuleResultDao()).thenReturn(moduleResultDao);
		assertSame(moduleResultDao, Kalibro.getModuleResultDao());
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