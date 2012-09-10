package org.kalibro.client;

import org.kalibro.core.dao.MetricConfigurationDao;
import org.kalibro.core.model.MetricConfiguration;
import org.kalibro.service.MetricConfigurationEndpoint;
import org.kalibro.service.entities.MetricConfigurationXml;

class MetricConfigurationPortDao extends EndpointClient<MetricConfigurationEndpoint> implements MetricConfigurationDao {

	protected MetricConfigurationPortDao(String serviceAddress) {
		super(serviceAddress, MetricConfigurationEndpoint.class);
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