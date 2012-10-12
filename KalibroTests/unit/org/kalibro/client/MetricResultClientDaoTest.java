package org.kalibro.client;

import static org.junit.Assert.assertEquals;

import java.util.*;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.kalibro.Metric;
import org.kalibro.MetricConfiguration;
import org.kalibro.MetricResult;
import org.kalibro.NativeMetric;
import org.kalibro.service.MetricResultEndpoint;
import org.kalibro.service.xml.DateMetricResultXml;
import org.kalibro.service.xml.MetricResultXml;
import org.kalibro.service.xml.MetricXmlRequest;
import org.mockito.ArgumentMatcher;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;

@PrepareOnlyThisForTest(MetricResultClientDao.class)
public class MetricResultClientDaoTest extends
	ClientTest<MetricResult, MetricResultXml, MetricResultXml, MetricResultEndpoint, MetricResultClientDao> {

	private static final Long ID = new Random().nextLong();

	@Override
	protected Class<MetricResult> entityClass() {
		return MetricResult.class;
	}

	@Test
	public void shouldGetMetricResultsOfModuleResult() {
		when(port.metricResultsOf(ID)).thenReturn(asList(response));
		assertDeepEquals(asSet(entity), client.metricResultsOf(ID));
	}

	@Test
	public void shouldGetHistoryOfMetric() {
		Date date = new Date(1);
		MetricResult result = new MetricResult(new MetricConfiguration(), 42.0);
		List<DateMetricResultXml> history = new ArrayList<DateMetricResultXml>();
		history.add(new DateMetricResultXml(date, result));

		Metric metric = loadFixture("cbo", NativeMetric.class);
		when(port.historyOf(argThat(convertsTo(metric)), eq(ID))).thenReturn(history);

		SortedMap<Date, MetricResult> map = client.historyOf(metric, ID);
		assertEquals(1, map.size());
		assertDeepEquals(asSet(date), map.keySet());
		assertDeepEquals(result, map.get(date));
	}

	private Matcher<MetricXmlRequest> convertsTo(final Metric metric) {
		return new ArgumentMatcher<MetricXmlRequest>() {

			@Override
			public boolean matches(Object argument) {
				return ((MetricXmlRequest) argument).convert().deepEquals(metric);
			}
		};
	}
}