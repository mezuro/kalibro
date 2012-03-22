package org.kalibro.service;

import static org.junit.Assert.*;
import static org.kalibro.core.model.MetricConfigurationFixtures.*;

import java.net.MalformedURLException;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.core.model.MetricConfiguration;
import org.kalibro.core.persistence.dao.MetricConfigurationDao;
import org.kalibro.core.persistence.dao.MetricConfigurationDaoStub;
import org.kalibro.service.entities.MetricConfigurationXml;

public class MetricConfigurationEndpointTest extends KalibroServiceTestCase {

	private static final String CONFIGURATION_NAME = "MetricConfigurationEndpointTest";

	private MetricConfigurationDao daoStub;
	private MetricConfigurationEndpoint port;

	@Before
	public void setUp() throws MalformedURLException {
		daoStub = new MetricConfigurationDaoStub();
		daoStub.save(configuration("cbo"), CONFIGURATION_NAME);
		daoStub.save(configuration("loc"), CONFIGURATION_NAME);
		port = publishAndGetPort(new MetricConfigurationEndpointImpl(daoStub), MetricConfigurationEndpoint.class);
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void shouldGetMetricConfigurationByName() {
		verifyGetMetricConfiguration("cbo");
		verifyGetMetricConfiguration("loc");
	}

	private void verifyGetMetricConfiguration(String code) {
		String metricName = configuration(code).getMetric().getName();
		MetricConfiguration expected = daoStub.getMetricConfiguration(CONFIGURATION_NAME, metricName);
		MetricConfiguration actual = port.getMetricConfiguration(CONFIGURATION_NAME, metricName).convert();
		assertDeepEquals(expected, actual);
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void shouldRemoveConfigurationByName() {
		verifyRemoveMetricConfiguration("cbo");
		verifyRemoveMetricConfiguration("loc");
	}

	private void verifyRemoveMetricConfiguration(String code) {
		String metricName = configuration(code).getMetric().getName();
		assertNotNull(daoStub.getMetricConfiguration(CONFIGURATION_NAME, metricName));
		port.removeMetricConfiguration(CONFIGURATION_NAME, metricName);
		assertNull(daoStub.getMetricConfiguration(CONFIGURATION_NAME, metricName));
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void shouldSaveConfiguration() {
		MetricConfiguration newConfiguration = configuration("acc");
		String metricName = newConfiguration.getMetric().getName();
		assertNull(daoStub.getMetricConfiguration(CONFIGURATION_NAME, metricName));
		port.saveMetricConfiguration(new MetricConfigurationXml(newConfiguration), CONFIGURATION_NAME);
		assertDeepEquals(newConfiguration, daoStub.getMetricConfiguration(CONFIGURATION_NAME, metricName));
	}
}