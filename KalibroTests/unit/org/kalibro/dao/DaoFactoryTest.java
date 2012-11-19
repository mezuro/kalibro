package org.kalibro.dao;

import static org.junit.Assert.assertSame;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.DatabaseSettings;
import org.kalibro.KalibroSettings;
import org.kalibro.ServiceSide;
import org.kalibro.client.ClientDaoFactory;
import org.kalibro.core.persistence.DatabaseDaoFactory;
import org.kalibro.tests.UnitTest;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

@RunWith(PowerMockRunner.class)
@PrepareForTest({DaoFactory.class, KalibroSettings.class})
public class DaoFactoryTest extends UnitTest {

	private DaoFactory daoFactory;

	@Before
	public void setUp() throws Exception {
		daoFactory = mockAbstract(DaoFactory.class);
		spy(DaoFactory.class);
		doReturn(daoFactory).when(DaoFactory.class, "createFactory");
	}

	@Test
	public void shouldGetDaos() throws Exception {
		shouldGetDao(BaseToolDao.class);
		shouldGetDao(ConfigurationDao.class);
		shouldGetDao(MetricConfigurationDao.class);
		shouldGetDao(MetricResultDao.class);
		shouldGetDao(ModuleResultDao.class);
		shouldGetDao(ProcessingDao.class);
		shouldGetDao(ProjectDao.class);
		shouldGetDao(RangeDao.class);
		shouldGetDao(ReadingDao.class);
		shouldGetDao(ReadingGroupDao.class);
		shouldGetDao(RepositoryDao.class);
	}

	private <T> void shouldGetDao(Class<T> daoClass) throws Exception {
		T dao = mock(daoClass);
		String name = daoClass.getSimpleName();
		doReturn(dao).when(daoFactory, method(DaoFactory.class, "create" + name)).withNoArguments();
		assertSame(dao, Whitebox.invokeMethod(DaoFactory.class, "get" + name));
	}

	@Test
	public void shouldCreatePortDaoFactoryOnClientSide() throws Exception {
		String serviceAddress = mockSettings(ServiceSide.CLIENT).getClientSettings().getServiceAddress();
		daoFactory = mock(ClientDaoFactory.class);
		whenNew(ClientDaoFactory.class).withArguments(serviceAddress).thenReturn((ClientDaoFactory) daoFactory);
		verifyFactory();
	}

	@Test
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
		doCallRealMethod().when(DaoFactory.class, "createFactory");
		assertSame(daoFactory, Whitebox.invokeMethod(DaoFactory.class, "createFactory"));
	}
}