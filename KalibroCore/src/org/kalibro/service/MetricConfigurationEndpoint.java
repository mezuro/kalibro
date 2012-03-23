package org.kalibro.service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.kalibro.service.entities.MetricConfigurationXml;

@WebService
public interface MetricConfigurationEndpoint {

	@WebMethod
	void saveMetricConfiguration(
		@WebParam(name = "metricConfiguration") MetricConfigurationXml metricConfiguration,
		@WebParam(name = "configurationName") String configurationName);

	@WebMethod
	@WebResult(name = "metricConfiguration")
	MetricConfigurationXml getMetricConfiguration(
		@WebParam(name = "configurationName") String configurationName,
		@WebParam(name = "metricName") String metricName);

	@WebMethod
	void removeMetricConfiguration(
		@WebParam(name = "configurationName") String configurationName,
		@WebParam(name = "metricName") String metricName);
}