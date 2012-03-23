package org.kalibro.client.dao;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.KalibroTestCase;
import org.kalibro.client.EndpointPortFactory;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(EndpointPortFactory.class)
public class PortDaoFactoryTest extends KalibroTestCase {

	private PortDaoFactory factory;

	@Before
	public void setUp() {
		PowerMockito.mockStatic(EndpointPortFactory.class);
		factory = new PortDaoFactory();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkDaoClasses() {
		assertClassEquals(BaseToolPortDao.class, factory.getBaseToolDao());
		assertClassEquals(ConfigurationPortDao.class, factory.getConfigurationDao());
		assertClassEquals(MetricConfigurationPortDao.class, factory.getMetricConfigurationDao());
		assertClassEquals(ProjectPortDao.class, factory.getProjectDao());
		assertClassEquals(ProjectResultPortDao.class, factory.getProjectResultDao());
		assertClassEquals(ModuleResultPortDao.class, factory.getModuleResultDao());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldInitializeOnlyOnce() {
		assertSame(factory.getBaseToolDao(), factory.getBaseToolDao());
		assertSame(factory.getConfigurationDao(), factory.getConfigurationDao());
		assertSame(factory.getMetricConfigurationDao(), factory.getMetricConfigurationDao());
		assertSame(factory.getProjectDao(), factory.getProjectDao());
		assertSame(factory.getProjectResultDao(), factory.getProjectResultDao());
		assertSame(factory.getModuleResultDao(), factory.getModuleResultDao());
	}
}