package org.kalibro.client;

import static org.junit.Assert.assertSame;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.TestCase;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ClientDaoFactory.class)
public class ClientDaoFactoryTest extends TestCase {

	private static final String SERVICE_ADDRESS = "ClientDaoFactoryTest service address";

	private ClientDaoFactory factory;

	@Before
	public void setUp() {
		factory = new ClientDaoFactory(SERVICE_ADDRESS);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldCreateBaseToolPortDao() throws Exception {
		assertSame(prepareDao(BaseToolClientDao.class), factory.createBaseToolDao());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldCreateConfigurationPortDao() throws Exception {
		assertSame(prepareDao(ConfigurationClientDao.class), factory.createConfigurationDao());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldCreateMetricConfigurationPortDao() throws Exception {
		assertSame(prepareDao(MetricConfigurationClientDao.class), factory.createMetricConfigurationDao());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldCreateModuleResultPortDao() throws Exception {
		assertSame(prepareDao(ModuleResultClientDao.class), factory.createModuleResultDao());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldCreateProjectPortDao() throws Exception {
		assertSame(prepareDao(ProjectClientDao.class), factory.createProjectDao());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldCreateProjectResultPortDao() throws Exception {
		assertSame(prepareDao(ProjectResultClientDao.class), factory.createProjectResultDao());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldCreateReadingPortDao() throws Exception {
		assertSame(prepareDao(ReadingClientDao.class), factory.createReadingDao());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldCreateReadingGroupPortDao() throws Exception {
		assertSame(prepareDao(ReadingGroupClientDao.class), factory.createReadingGroupDao());
	}

	private <T> T prepareDao(Class<T> daoClass) throws Exception {
		T dao = mock(daoClass);
		whenNew(daoClass).withArguments(SERVICE_ADDRESS).thenReturn(dao);
		return dao;
	}
}