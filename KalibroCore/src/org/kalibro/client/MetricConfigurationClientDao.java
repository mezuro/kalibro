package org.kalibro.client;

import java.util.SortedSet;

import org.kalibro.MetricConfiguration;
import org.kalibro.dao.MetricConfigurationDao;
import org.kalibro.dto.DataTransferObject;
import org.kalibro.service.MetricConfigurationEndpoint;
import org.kalibro.service.xml.MetricConfigurationXml;

/**
 * {@link MetricConfigurationEndpoint} client implementation of
 * {@link MetricConfigurationDao}.
 * 
 * @author Carlos Morais
 */
class MetricConfigurationClientDao extends
	EndpointClient<MetricConfigurationEndpoint> implements
	MetricConfigurationDao {

    MetricConfigurationClientDao(String serviceAddress) {
	super(serviceAddress, MetricConfigurationEndpoint.class);
    }

    @Override
    public SortedSet<MetricConfiguration> metricConfigurationsOf(
	    Long configurationId) {
	return DataTransferObject.toSortedSet(port
		.metricConfigurationsOf(configurationId));
    }

    @Override
    public Long save(MetricConfiguration metricConfiguration,
	    Long configurationId) {
	return port.saveMetricConfiguration(new MetricConfigurationXml(
		metricConfiguration), configurationId);
    }

    @Override
    public void delete(Long metricConfigurationId) {
	port.deleteMetricConfiguration(metricConfigurationId);
    }

    @Override
    public MetricConfiguration get(Long metricConfigurationId) {
	return port.getMetricConfiguration(metricConfigurationId).convert();
    }
}