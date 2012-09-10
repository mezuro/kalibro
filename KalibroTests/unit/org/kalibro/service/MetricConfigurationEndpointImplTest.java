package org.kalibro.service;

import static org.kalibro.core.model.MetricConfigurationFixtures.metricConfiguration;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.TestCase;
import org.kalibro.core.model.MetricConfiguration;
import org.kalibro.dao.DaoFactory;
import org.kalibro.dao.MetricConfigurationDao;
import org.kalibro.service.entities.MetricConfigurationXml;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DaoFactory.class)
public class MetricConfigurationEndpointImplTest extends TestCase {

	private static final String CONFIGURATION_NAME = "MetricConfigurationEndpointImplTest";

	private MetricConfigurationDao dao;
	private MetricConfiguration configuration;
	private MetricConfigurationEndpointImpl endpoint;

	@Before
	public void setUp() {
		mockDao();
		configuration = metricConfiguration("cbo");
		endpoint = new MetricConfigurationEndpointImpl();
	}

	private void mockDao() {
		dao = PowerMockito.mock(MetricConfigurationDao.class);
		PowerMockito.mockStatic(DaoFactory.class);
		PowerMockito.when(DaoFactory.getMetricConfigurationDao()).thenReturn(dao);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testSaveConfiguration() {
		endpoint.saveMetricConfiguration(new MetricConfigurationXml(configuration), CONFIGURATION_NAME);
		Mockito.verify(dao).save(configuration, CONFIGURATION_NAME);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testGetConfiguration() {
		String metricName = configuration.getMetric().getName();
		PowerMockito.when(dao.getMetricConfiguration(CONFIGURATION_NAME, metricName)).thenReturn(configuration);
		assertDeepEquals(configuration, endpoint.getMetricConfiguration(CONFIGURATION_NAME, metricName).convert());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testRemoveConfiguration() {
		String metricName = configuration.getMetric().getName();
		endpoint.removeMetricConfiguration(CONFIGURATION_NAME, metricName);
		Mockito.verify(dao).removeMetricConfiguration(CONFIGURATION_NAME, metricName);
	}
}