package org.kalibro.service;

import static org.junit.Assert.*;
import static org.kalibro.core.model.MetricConfigurationFixtures.*;

import java.net.MalformedURLException;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.core.model.MetricConfiguration;
import org.kalibro.core.persistence.dao.MetricConfigurationDao;
import org.kalibro.core.persistence.dao.MetricConfigurationDaoFake;
import org.kalibro.service.entities.MetricConfigurationXml;

public class MetricConfigurationEndpointTest extends KalibroServiceTestCase {

	private static final String CONFIGURATION_NAME = "MetricConfigurationEndpointTest";

	private MetricConfigurationDao daoFake;
	private MetricConfigurationEndpoint port;

	@Before
	public void setUp() throws MalformedURLException {
		daoFake = new MetricConfigurationDaoFake();
		daoFake.save(metricConfiguration("cbo"), CONFIGURATION_NAME);
		daoFake.save(metricConfiguration("loc"), CONFIGURATION_NAME);
		port = publishAndGetPort(new MetricConfigurationEndpointImpl(daoFake), MetricConfigurationEndpoint.class);
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void shouldGetMetricConfigurationByName() {
		verifyGetMetricConfiguration("cbo");
		verifyGetMetricConfiguration("loc");
	}

	private void verifyGetMetricConfiguration(String code) {
		String metricName = metricConfiguration(code).getMetric().getName();
		MetricConfiguration expected = daoFake.getMetricConfiguration(CONFIGURATION_NAME, metricName);
		MetricConfiguration actual = port.getMetricConfiguration(CONFIGURATION_NAME, metricName).convert();
		assertDeepEquals(expected, actual);
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void shouldRemoveConfigurationByName() {
		verifyRemoveMetricConfiguration("cbo");
		verifyRemoveMetricConfiguration("loc");
	}

	private void verifyRemoveMetricConfiguration(String code) {
		String metricName = metricConfiguration(code).getMetric().getName();
		assertNotNull(daoFake.getMetricConfiguration(CONFIGURATION_NAME, metricName));
		port.removeMetricConfiguration(CONFIGURATION_NAME, metricName);
		assertNull(daoFake.getMetricConfiguration(CONFIGURATION_NAME, metricName));
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void shouldSaveConfiguration() {
		MetricConfiguration newConfiguration = metricConfiguration("acc");
		String metricName = newConfiguration.getMetric().getName();
		assertNull(daoFake.getMetricConfiguration(CONFIGURATION_NAME, metricName));
		port.saveMetricConfiguration(new MetricConfigurationXml(newConfiguration), CONFIGURATION_NAME);
		assertDeepEquals(newConfiguration, daoFake.getMetricConfiguration(CONFIGURATION_NAME, metricName));
	}
}