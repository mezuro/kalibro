package org.kalibro.service;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;
import org.kalibro.MetricConfiguration;
import org.kalibro.MetricResult;
import org.kalibro.dao.MetricResultDao;
import org.kalibro.dto.DaoLazyLoader;
import org.kalibro.service.xml.DateMetricResultXml;
import org.kalibro.service.xml.MetricResultXml;
import org.powermock.core.classloader.annotations.PrepareForTest;

@PrepareForTest({DaoLazyLoader.class, MetricResultEndpointImpl.class})
public class MetricResultEndpointImplTest extends
	EndpointImplementorTest<MetricResult, MetricResultXml, MetricResultXml, MetricResultDao, MetricResultEndpointImpl> {

	private static final Long ID = new Random().nextLong();
	private static final String METRIC_NAME = "MetricResultEndpointImplTest metric name";

	@Override
	protected Class<MetricResult> entityClass() {
		return MetricResult.class;
	}

	@Test
	public void shouldGetDescendantResultsOfMetricResult() {
		List<Double> descendantResults = mock(List.class);
		when(dao.descendantResultsOf(ID)).thenReturn(descendantResults);
		assertSame(descendantResults, implementor.descendantResultsOf(ID));
	}

	@Test
	public void shouldGetMetricResultsOfModuleResult() {
		when(dao.metricResultsOf(ID)).thenReturn(sortedSet(entity));
		assertDeepEquals(list(response), implementor.metricResultsOf(ID));
	}

	@Test
	public void shouldGetHistoryOfMetric() {
		mockLazyLoad();

		Date date = new Date(1);
		MetricResult result = new MetricResult(new MetricConfiguration(), 42.0);
		SortedMap<Date, MetricResult> map = new TreeMap<Date, MetricResult>();
		map.put(date, result);

		when(dao.historyOf(METRIC_NAME, ID)).thenReturn(map);
		List<DateMetricResultXml> history = implementor.historyOf(METRIC_NAME, ID);
		assertEquals(1, history.size());
		assertEquals(date, history.get(0).date());
		assertDeepEquals(result, history.get(0).metricResult());
	}

	private void mockLazyLoad() {
		mockStatic(DaoLazyLoader.class);
		when(DaoLazyLoader.createProxy(MetricResultDao.class, "descendantResultsOf", (Long) null)).thenReturn(list());
	}
}