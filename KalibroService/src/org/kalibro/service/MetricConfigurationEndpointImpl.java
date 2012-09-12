package org.kalibro.service;

import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.kalibro.dao.DaoFactory;
import org.kalibro.dao.MetricConfigurationDao;
import org.kalibro.service.entities.MetricConfigurationXml;

@WebService(name = "MetricConfigurationEndpoint", serviceName = "MetricConfigurationEndpointService")
public class MetricConfigurationEndpointImpl implements MetricConfigurationEndpoint {

	private MetricConfigurationDao dao;

	public MetricConfigurationEndpointImpl() {
		this(DaoFactory.getMetricConfigurationDao());
	}

	public MetricConfigurationEndpointImpl(MetricConfigurationDao metricConfigurationDao) {
		dao = metricConfigurationDao;
	}

	@Override
	public void saveMetricConfiguration(
		@WebParam(name = "metricConfiguration") MetricConfigurationXml metricConfiguration,
		@WebParam(name = "configurationName") String configurationName) {
		dao.save(metricConfiguration.convert(), configurationName);
	}

	@Override
	@WebResult(name = "metricConfiguration")
	public MetricConfigurationXml getMetricConfiguration(
		@WebParam(name = "configurationName") String configurationName,
		@WebParam(name = "metricName") String metricName) {
		return new MetricConfigurationXml(dao.getMetricConfiguration(configurationName, metricName));
	}

	@Override
	public void removeMetricConfiguration(
		@WebParam(name = "configurationName") String configurationName,
		@WebParam(name = "metricName") String metricName) {
		dao.removeMetricConfiguration(configurationName, metricName);
	}
}