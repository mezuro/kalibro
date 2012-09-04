package org.kalibro.client.dao;

import org.kalibro.client.EndpointPortFactory;
import org.kalibro.core.model.MetricConfiguration;
import org.kalibro.core.persistence.dao.MetricConfigurationDao;
import org.kalibro.service.MetricConfigurationEndpoint;
import org.kalibro.service.entities.MetricConfigurationXml;

class MetricConfigurationPortDao implements MetricConfigurationDao {

	private MetricConfigurationEndpoint port;

	protected MetricConfigurationPortDao() {
		port = EndpointPortFactory.getEndpointPort(MetricConfigurationEndpoint.class);
	}

	@Override
	public void save(MetricConfiguration metricConfiguration, String configurationName) {
		port.saveMetricConfiguration(new MetricConfigurationXml(metricConfiguration), configurationName);
	}

	@Override
	public MetricConfiguration getMetricConfiguration(String configurationName, String metricName) {
		return port.getMetricConfiguration(configurationName, metricName).convert();
	}

	@Override
	public void removeMetricConfiguration(String configurationName, String metricName) {
		port.removeMetricConfiguration(configurationName, metricName);
	}
}