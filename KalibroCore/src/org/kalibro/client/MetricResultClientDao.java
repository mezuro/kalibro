package org.kalibro.client;

import java.util.*;

import org.kalibro.MetricResult;
import org.kalibro.dao.MetricResultDao;
import org.kalibro.dto.DataTransferObject;
import org.kalibro.service.MetricResultEndpoint;
import org.kalibro.service.xml.DateMetricResultXml;

/**
 * {@link MetricResultEndpoint} client implementation of {@link MetricResultDao}.
 * 
 * @author Carlos Morais
 */
class MetricResultClientDao extends EndpointClient<MetricResultEndpoint> implements MetricResultDao {

	MetricResultClientDao(String serviceAddress) {
		super(serviceAddress, MetricResultEndpoint.class);
	}

	@Override
	public List<Double> descendantResultsOf(Long metricResultId) {
		return port.descendantResultsOf(metricResultId);
	}

	@Override
	public SortedSet<MetricResult> metricResultsOf(Long moduleResultId) {
		return DataTransferObject.toSortedSet(port.metricResultsOf(moduleResultId));
	}

	@Override
	public SortedMap<Date, MetricResult> historyOf(String metricName, Long moduleResultId) {
		SortedMap<Date, MetricResult> history = new TreeMap<Date, MetricResult>();
		for (DateMetricResultXml dateResult : port.historyOf(metricName, moduleResultId))
			history.put(dateResult.date(), dateResult.metricResult());
		return history;
	}
}