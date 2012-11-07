package org.kalibro.service;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.kalibro.dao.MetricConfigurationDao;
import org.kalibro.service.xml.MetricConfigurationXml;

/**
 * End point to make {@link MetricConfigurationDao} interface available as Web service.
 * 
 * @author Carlos Morais
 */
@WebService(name = "MetricConfigurationEndpoint", serviceName = "MetricConfigurationEndpointService")
public interface MetricConfigurationEndpoint {

	@WebMethod
	@WebResult(name = "metricConfiguration")
	List<MetricConfigurationXml> metricConfigurationsOf(
		@WebParam(name = "configurationId") Long configurationId);

	@WebMethod
	@WebResult(name = "metricConfigurationId")
	Long saveMetricConfiguration(
		@WebParam(name = "metricConfiguration") MetricConfigurationXml metricConfiguration,
		@WebParam(name = "configurationId") Long configurationId);

	@WebMethod
	void deleteMetricConfiguration(@WebParam(name = "metricConfigurationId") Long metricConfigurationId);
}