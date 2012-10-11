package org.kalibro.service;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.kalibro.dao.MetricResultDao;
import org.kalibro.service.xml.MetricResultXml;

/**
 * End point to make {@link MetricResultDao} interface available as Web service.
 * 
 * @author Carlos Morais
 */
@WebService(name = "MetricResultEndpoint", serviceName = "MetricResultEndpointService")
public interface MetricResultEndpoint {

	@WebMethod
	@WebResult(name = "metricResult")
	List<MetricResultXml> metricResultsOf(@WebParam(name = "moduleResultId") Long moduleResultId);

	// TODO SortedMap<Date, MetricResult> historyOf(Metric metric, Long repositoryId);
}