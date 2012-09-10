package org.kalibro.core.dao;

import static org.junit.Assert.assertSame;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.DatabaseSettings;
import org.kalibro.KalibroSettings;
import org.kalibro.ServiceSide;
import org.kalibro.TestCase;
import org.kalibro.client.ClientDaoFactory;
import org.kalibro.core.persistence.DatabaseDaoFactory;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

@RunWith(PowerMockRunner.class)
@PrepareForTest({DaoFactory.class, KalibroSettings.class})
public class DaoFactoryTest extends TestCase {

	private DaoFactory daoFactory;

	@Before
	public void setUp() throws Exception {
		daoFactory = mock(DaoFactory.class);
		spy(DaoFactory.class);
		doReturn(daoFactory).when(DaoFactory.class, "getFactory");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldGetBaseToolDao() {
		BaseToolDao baseToolDao = mock(BaseToolDao.class);
		when(daoFactory.createBaseToolDao()).thenReturn(baseToolDao);
		assertSame(baseToolDao, DaoFactory.getBaseToolDao());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldGetConfigurationDao() {
		ConfigurationDao configurationDao = mock(ConfigurationDao.class);
		when(daoFactory.createConfigurationDao()).thenReturn(configurationDao);
		assertSame(configurationDao, DaoFactory.getConfigurationDao());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldGetMetricConfigurationDao() {
		MetricConfigurationDao metricConfigurationDao = mock(MetricConfigurationDao.class);
		when(daoFactory.createMetricConfigurationDao()).thenReturn(metricConfigurationDao);
		assertSame(metricConfigurationDao, DaoFactory.getMetricConfigurationDao());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldGetModuleResultDao() {
		ModuleResultDao moduleResultDao = mock(ModuleResultDao.class);
		when(daoFactory.createModuleResultDao()).thenReturn(moduleResultDao);
		assertSame(moduleResultDao, DaoFactory.getModuleResultDao());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldGetProjectDao() {
		ProjectDao projectDao = mock(ProjectDao.class);
		when(daoFactory.createProjectDao()).thenReturn(projectDao);
		assertSame(projectDao, DaoFactory.getProjectDao());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldGetProjectResultDao() {
		ProjectResultDao projectResultDao = mock(ProjectResultDao.class);
		when(daoFactory.createProjectResultDao()).thenReturn(projectResultDao);
		assertSame(projectResultDao, DaoFactory.getProjectResultDao());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldGetReadingDao() {
		ReadingDao readingDao = mock(ReadingDao.class);
		when(daoFactory.createReadingDao()).thenReturn(readingDao);
		assertSame(readingDao, DaoFactory.getReadingDao());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldGetReadingGroupDao() {
		ReadingGroupDao readingGroupDao = mock(ReadingGroupDao.class);
		when(daoFactory.createReadingGroupDao()).thenReturn(readingGroupDao);
		assertSame(readingGroupDao, DaoFactory.getReadingGroupDao());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldCreatePortDaoFactoryOnClientSide() throws Exception {
		String serviceAddress = mockSettings(ServiceSide.CLIENT).getClientSettings().getServiceAddress();
		daoFactory = mock(ClientDaoFactory.class);
		whenNew(ClientDaoFactory.class).withArguments(serviceAddress).thenReturn((ClientDaoFactory) daoFactory);
		verifyFactory();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldCreateDatabaseDaoFactoryOnServerSide() throws Exception {
		DatabaseSettings settings = mockSettings(ServiceSide.SERVER).getServerSettings().getDatabaseSettings();
		daoFactory = mock(DatabaseDaoFactory.class);
		whenNew(DatabaseDaoFactory.class).withArguments(settings).thenReturn((DatabaseDaoFactory) daoFactory);
		verifyFactory();
	}

	private KalibroSettings mockSettings(ServiceSide serviceSide) {
		KalibroSettings settings = new KalibroSettings();
		settings.setServiceSide(serviceSide);

		mockStatic(KalibroSettings.class);
		when(KalibroSettings.load()).thenReturn(settings);
		return settings;
	}

	private void verifyFactory() throws Exception {
		doCallRealMethod().when(DaoFactory.class, "getFactory");
		assertSame(daoFactory, Whitebox.invokeMethod(DaoFactory.class, "getFactory"));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void constructorCoverage() {
		new DummyDaoFactory();
	}
}