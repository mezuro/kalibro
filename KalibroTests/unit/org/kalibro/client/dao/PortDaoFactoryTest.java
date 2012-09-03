package org.kalibro.client.dao;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.TestCase;
import org.kalibro.client.EndpointPortFactory;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(EndpointPortFactory.class)
public class PortDaoFactoryTest extends TestCase {

	private PortDaoFactory factory;

	@Before
	public void setUp() {
		PowerMockito.mockStatic(EndpointPortFactory.class);
		factory = new PortDaoFactory();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkDaoClasses() {
		assertClassEquals(BaseToolPortDao.class, factory.createBaseToolDao());
		assertClassEquals(ConfigurationPortDao.class, factory.createConfigurationDao());
		assertClassEquals(MetricConfigurationPortDao.class, factory.createMetricConfigurationDao());
		assertClassEquals(ProjectPortDao.class, factory.createProjectDao());
		assertClassEquals(ProjectResultPortDao.class, factory.createProjectResultDao());
		assertClassEquals(ModuleResultPortDao.class, factory.createModuleResultDao());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldInitializeOnlyOnce() {
		assertSame(factory.createBaseToolDao(), factory.createBaseToolDao());
		assertSame(factory.createConfigurationDao(), factory.createConfigurationDao());
		assertSame(factory.createMetricConfigurationDao(), factory.createMetricConfigurationDao());
		assertSame(factory.createProjectDao(), factory.createProjectDao());
		assertSame(factory.createProjectResultDao(), factory.createProjectResultDao());
		assertSame(factory.createModuleResultDao(), factory.createModuleResultDao());
	}
}