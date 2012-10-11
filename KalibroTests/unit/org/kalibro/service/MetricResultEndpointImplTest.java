package org.kalibro.service;

import static org.junit.Assert.assertEquals;

import java.util.*;

import org.junit.Test;
import org.kalibro.Metric;
import org.kalibro.MetricConfiguration;
import org.kalibro.MetricResult;
import org.kalibro.NativeMetric;
import org.kalibro.dao.MetricResultDao;
import org.kalibro.service.xml.DateMetricResultXml;
import org.kalibro.service.xml.MetricResultXml;
import org.kalibro.service.xml.MetricXmlRequest;
import org.powermock.core.classloader.annotations.PrepareForTest;

@PrepareForTest(MetricResultEndpointImpl.class)
public class MetricResultEndpointImplTest extends
	EndpointImplementorTest<MetricResult, MetricResultXml, MetricResultXml, MetricResultDao, MetricResultEndpointImpl> {

	private static final Long ID = new Random().nextLong();

	@Override
	protected Class<MetricResult> entityClass() {
		return MetricResult.class;
	}

	@Test
	public void shouldGetMetricResultsOfModuleResult() {
		when(dao.metricResultsOf(ID)).thenReturn(asSortedSet(entity));
		assertDeepEquals(asList(response), implementor.metricResultsOf(ID));
	}

	@Test
	public void shouldGetHistoryOfMetric() {
		Metric metric = loadFixture("cbo", NativeMetric.class);
		MetricXmlRequest metricXml = new MetricXmlRequest(metric);

		Date date = new Date(1);
		MetricResult result = new MetricResult(new MetricConfiguration(), 42.0);
		SortedMap<Date, MetricResult> map = new TreeMap<Date, MetricResult>();
		map.put(date, result);

		when(dao.historyOf(metric, ID)).thenReturn(map);
		List<DateMetricResultXml> history = implementor.historyOf(metricXml, ID);
		assertEquals(1, history.size());
		assertEquals(date, history.get(0).date());
		assertDeepEquals(result, history.get(0).metricResult());
	}
}