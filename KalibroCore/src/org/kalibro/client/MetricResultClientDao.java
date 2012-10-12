package org.kalibro.client;

import java.util.Date;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;

import org.kalibro.Metric;
import org.kalibro.MetricResult;
import org.kalibro.dao.MetricResultDao;
import org.kalibro.dto.DataTransferObject;
import org.kalibro.service.MetricResultEndpoint;
import org.kalibro.service.xml.DateMetricResultXml;
import org.kalibro.service.xml.MetricXmlRequest;

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
	public SortedSet<MetricResult> metricResultsOf(Long moduleResultId) {
		return DataTransferObject.toSortedSet(port.metricResultsOf(moduleResultId));
	}

	@Override
	public SortedMap<Date, MetricResult> historyOf(Metric metric, Long repositoryId) {
		SortedMap<Date, MetricResult> history = new TreeMap<Date, MetricResult>();
		for (DateMetricResultXml dateResult : port.historyOf(new MetricXmlRequest(metric), repositoryId))
			history.put(dateResult.date(), dateResult.metricResult());
		return history;
	}
}