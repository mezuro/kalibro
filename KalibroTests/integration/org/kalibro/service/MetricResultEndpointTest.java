package org.kalibro.service;

import static org.junit.Assert.assertEquals;

import java.util.*;

import org.junit.Test;
import org.kalibro.*;
import org.kalibro.client.EndpointTest;
import org.kalibro.dao.MetricResultDao;
import org.kalibro.service.xml.DateMetricResultXml;
import org.kalibro.service.xml.MetricXmlRequest;
import org.powermock.reflect.Whitebox;

public class MetricResultEndpointTest extends EndpointTest<MetricResult, MetricResultDao, MetricResultEndpoint> {

	private static final Long ID = new Random().nextLong();

	@Override
	protected MetricResult loadFixture() {
		MetricConfiguration configuration = loadFixture("lcom4", MetricConfiguration.class);
		configuration.setReadingGroup(null);
		Whitebox.setInternalState(configuration, "baseTool", (BaseTool) null);
		MetricResult metricResult = new MetricResult(configuration, new Random().nextDouble());
		metricResult.addDescendentResult(42.0);
		return metricResult;
	}

	@Test
	public void shouldGetMetricResultsOfModuleResult() {
		when(dao.metricResultsOf(ID)).thenReturn(asSortedSet(entity));
		assertDeepDtoList(asList(entity), port.metricResultsOf(ID));
	}

	@Test
	public void shouldGetMetricResultHistory() {
		Metric metric = loadFixture("cbo", NativeMetric.class);
		MetricXmlRequest metricXml = new MetricXmlRequest(metric);

		Date date = new Date(1);
		MetricResult result = new MetricResult(new MetricConfiguration(), 42.0);
		SortedMap<Date, MetricResult> map = new TreeMap<Date, MetricResult>();
		map.put(date, result);

		when(dao.historyOf(metric, ID)).thenReturn(map);
		List<DateMetricResultXml> history = port.historyOf(metricXml, ID);
		assertEquals(1, history.size());
		assertEquals(date, history.get(0).date());
		assertDeepEquals(result, history.get(0).metricResult());
	}
}