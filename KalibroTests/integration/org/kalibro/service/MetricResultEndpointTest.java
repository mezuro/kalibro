package org.kalibro.service;

import static org.junit.Assert.assertEquals;

import java.util.*;

import org.junit.Test;
import org.kalibro.Metric;
import org.kalibro.MetricConfiguration;
import org.kalibro.MetricResult;
import org.kalibro.NativeMetric;
import org.kalibro.client.EndpointTest;
import org.kalibro.dao.MetricResultDao;
import org.kalibro.service.xml.DateMetricResultXml;
import org.kalibro.service.xml.MetricResultXml;
import org.kalibro.service.xml.MetricXmlRequest;
import org.powermock.reflect.Whitebox;

public class MetricResultEndpointTest extends EndpointTest<MetricResult, MetricResultDao, MetricResultEndpoint> {

	private static final Long ID = new Random().nextLong();

	@Override
	protected MetricResult loadFixture() {
		MetricConfiguration configuration = loadFixture("lcom4", MetricConfiguration.class);
		MetricResult metricResult = new MetricResult(configuration, new Random().nextDouble());
		metricResult.addDescendantResult(42.0);
		Whitebox.setInternalState(metricResult, "id", ID);
		return metricResult;
	}

	@Override
	protected List<String> fieldsThatShouldBeProxy() {
		return asList("descendantResults");
	}

	@Test
	public void shouldGetDescendantResultsOfMetricResult() {
		List<Double> descendantResults = asList(6.0, 28.0, 496.0);
		when(dao.descendantResultsOf(ID)).thenReturn(descendantResults);
		assertDeepEquals(descendantResults, port.descendantResultsOf(ID));
	}

	@Test
	public void shouldGetMetricResultsOfModuleResult() {
		when(dao.metricResultsOf(ID)).thenReturn(asSortedSet(entity));
		List<MetricResultXml> metricResults = port.metricResultsOf(ID);
		assertEquals(1, metricResults.size());
		assertDeepDtoEquals(metricResults.get(0));
	}

	@Test
	public void shouldGetMetricResultHistory() {
		Metric metric = loadFixture("cbo", NativeMetric.class);
		MetricXmlRequest metricXml = new MetricXmlRequest(metric);

		Date date = new Date(1);
		SortedMap<Date, MetricResult> map = new TreeMap<Date, MetricResult>();
		map.put(date, entity);

		when(dao.historyOf(metric, ID)).thenReturn(map);
		List<DateMetricResultXml> history = port.historyOf(metricXml, ID);
		assertEquals(1, history.size());
		assertEquals(date, history.get(0).date());
		assertDeepDtoEquals(Whitebox.getInternalState(history.get(0), MetricResultXml.class));
	}

	private void assertDeepDtoEquals(MetricResultXml xml) {
		MetricResultXml spy = spy(xml);
		doReturn(entity.getConfiguration()).when(spy).configuration();
		assertDeepDtoEquals(entity, spy);
	}
}