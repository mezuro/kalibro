package org.kalibro.service;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.kalibro.dao.MetricResultDao;
import org.kalibro.service.xml.DateMetricResultXml;
import org.kalibro.service.xml.MetricResultXml;
import org.kalibro.service.xml.MetricXmlRequest;

/**
 * End point to make {@link MetricResultDao} interface available as Web service.
 * 
 * @author Carlos Morais
 */
@WebService(name = "MetricResultEndpoint", serviceName = "MetricResultEndpointService")
public interface MetricResultEndpoint {

	@WebMethod
	@WebResult(name = "descendantResult")
	List<Double> descendantResultsOf(@WebParam(name = "metricResultId") Long metricResultId);

	@WebMethod
	@WebResult(name = "metricResult")
	List<MetricResultXml> metricResultsOf(@WebParam(name = "moduleResultId") Long moduleResultId);

	@WebMethod
	@WebResult(name = "dateMetricResult")
	List<DateMetricResultXml> historyOf(
		@WebParam(name = "metric") MetricXmlRequest metric,
		@WebParam(name = "repositoryId") Long repositoryId);
}