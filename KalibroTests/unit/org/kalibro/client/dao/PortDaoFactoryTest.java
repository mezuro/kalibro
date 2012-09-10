package org.kalibro.client.dao;

import static org.junit.Assert.assertSame;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.TestCase;
import org.kalibro.client.*;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(PortDaoFactory.class)
public class PortDaoFactoryTest extends TestCase {

	private static final String SERVICE_ADDRESS = "PortDaoFactoryTest service address";

	private PortDaoFactory factory;

	@Before
	public void setUp() {
		factory = new PortDaoFactory(SERVICE_ADDRESS);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldCreateBaseToolPortDao() throws Exception {
		assertSame(prepareDao(BaseToolPortDao.class), factory.createBaseToolDao());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldCreateConfigurationPortDao() throws Exception {
		assertSame(prepareDao(ConfigurationPortDao.class), factory.createConfigurationDao());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldCreateMetricConfigurationPortDao() throws Exception {
		assertSame(prepareDao(MetricConfigurationPortDao.class), factory.createMetricConfigurationDao());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldCreateModuleResultPortDao() throws Exception {
		assertSame(prepareDao(ModuleResultPortDao.class), factory.createModuleResultDao());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldCreateProjectPortDao() throws Exception {
		assertSame(prepareDao(ProjectPortDao.class), factory.createProjectDao());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldCreateProjectResultPortDao() throws Exception {
		assertSame(prepareDao(ProjectResultPortDao.class), factory.createProjectResultDao());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldCreateReadingPortDao() throws Exception {
		assertSame(prepareDao(ReadingPortDao.class), factory.createReadingDao());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldCreateReadingGroupPortDao() throws Exception {
		assertSame(prepareDao(ReadingGroupPortDao.class), factory.createReadingGroupDao());
	}

	private <T> T prepareDao(Class<T> daoClass) throws Exception {
		T dao = mock(daoClass);
		whenNew(daoClass).withArguments(SERVICE_ADDRESS).thenReturn(dao);
		return dao;
	}
}