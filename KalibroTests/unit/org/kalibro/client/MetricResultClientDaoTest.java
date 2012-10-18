package org.kalibro.client;

import static org.junit.Assert.assertEquals;

import java.util.*;

import org.junit.Test;
import org.kalibro.MetricResult;
import org.kalibro.service.MetricResultEndpoint;
import org.kalibro.service.xml.DateMetricResultXml;
import org.kalibro.service.xml.MetricResultXml;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;

@PrepareOnlyThisForTest({DateMetricResultXml.class, MetricResultClientDao.class})
public class MetricResultClientDaoTest extends
	ClientTest<MetricResult, MetricResultXml, MetricResultXml, MetricResultEndpoint, MetricResultClientDao> {

	private static final Long ID = new Random().nextLong();
	private static final String METRIC_NAME = "MetricResultClientDaoTest metric name";

	@Override
	protected Class<MetricResult> entityClass() {
		return MetricResult.class;
	}

	@Test
	public void shouldGetDescendantResultsOfMetricResult() {
		List<Double> descendantResults = mock(List.class);
		when(port.descendantResultsOf(ID)).thenReturn(descendantResults);
		assertDeepEquals(descendantResults, client.descendantResultsOf(ID));
	}

	@Test
	public void shouldGetMetricResultsOfModuleResult() {
		when(port.metricResultsOf(ID)).thenReturn(asList(response));
		assertDeepEquals(asSet(entity), client.metricResultsOf(ID));
	}

	@Test
	public void shouldGetHistoryOfMetric() {
		Date date = new Date(1);
		List<DateMetricResultXml> history = new ArrayList<DateMetricResultXml>();
		history.add(new DateMetricResultXml(date, entity));
		when(request.convert()).thenReturn(entity);
		when(port.historyOf(METRIC_NAME, ID)).thenReturn(history);

		SortedMap<Date, MetricResult> map = client.historyOf(METRIC_NAME, ID);
		assertEquals(1, map.size());
		assertDeepEquals(asSet(date), map.keySet());
		assertDeepEquals(entity, map.get(date));
	}
}