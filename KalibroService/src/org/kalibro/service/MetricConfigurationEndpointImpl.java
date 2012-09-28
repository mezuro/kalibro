package org.kalibro.service;

import java.util.List;

import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.kalibro.dao.DaoFactory;
import org.kalibro.dao.MetricConfigurationDao;
import org.kalibro.dto.DataTransferObject;
import org.kalibro.service.xml.MetricConfigurationXmlRequest;
import org.kalibro.service.xml.MetricConfigurationXmlResponse;

/**
 * Implementation of {@link MetricConfigurationEndpoint}.
 * 
 * @author Carlos Morais
 */
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
	@WebResult(name = "metricConfiguration")
	public List<MetricConfigurationXmlResponse> metricConfigurationsOf(
		@WebParam(name = "configurationId") Long configurationId) {
		return DataTransferObject.createDtos(
			dao.metricConfigurationsOf(configurationId), MetricConfigurationXmlResponse.class);
	}

	@Override
	@WebResult(name = "metricConfigurationId")
	public Long saveMetricConfiguration(
		@WebParam(name = "metricConfiguration") MetricConfigurationXmlRequest metricConfiguration) {
		return dao.save(metricConfiguration.convert());
	}

	@Override
	public void deleteMetricConfiguration(@WebParam(name = "metricConfigurationId") Long metricConfigurationId) {
		dao.delete(metricConfigurationId);
	}
}