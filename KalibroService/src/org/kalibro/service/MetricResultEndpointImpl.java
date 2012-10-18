package org.kalibro.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;

import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.kalibro.MetricResult;
import org.kalibro.dao.DaoFactory;
import org.kalibro.dao.MetricResultDao;
import org.kalibro.dto.DataTransferObject;
import org.kalibro.service.xml.DateMetricResultXml;
import org.kalibro.service.xml.MetricResultXml;

/**
 * Implementation of {@link MetricResultEndpoint}.
 * 
 * @author Carlos Morais
 */
@WebService(name = "MetricResultEndpoint", serviceName = "MetricResultEndpointService")
public class MetricResultEndpointImpl implements MetricResultEndpoint {

	private MetricResultDao dao;

	public MetricResultEndpointImpl() {
		this(DaoFactory.getMetricResultDao());
	}

	public MetricResultEndpointImpl(MetricResultDao moduleResultDao) {
		dao = moduleResultDao;
	}

	@Override
	@WebResult(name = "descendantResult")
	public List<Double> descendantResultsOf(@WebParam(name = "metricResultId") Long metricResultId) {
		return dao.descendantResultsOf(metricResultId);
	}

	@Override
	@WebResult(name = "metricResult")
	public List<MetricResultXml> metricResultsOf(@WebParam(name = "moduleResultId") Long moduleResultId) {
		return DataTransferObject.createDtos(dao.metricResultsOf(moduleResultId), MetricResultXml.class);
	}

	@Override
	@WebResult(name = "dateMetricResult")
	public List<DateMetricResultXml> historyOf(
		@WebParam(name = "metricName") String metricName,
		@WebParam(name = "moduleResultId") Long moduleResultId) {
		SortedMap<Date, MetricResult> history = dao.historyOf(metricName, moduleResultId);
		List<DateMetricResultXml> dtos = new ArrayList<DateMetricResultXml>();
		for (Date date : history.keySet())
			dtos.add(new DateMetricResultXml(date, history.get(date)));
		return dtos;
	}
}