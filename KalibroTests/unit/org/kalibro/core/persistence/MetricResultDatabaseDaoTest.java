package org.kalibro.core.persistence;

import static org.junit.Assert.assertEquals;

import java.util.*;

import javax.persistence.TypedQuery;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.MetricResult;
import org.kalibro.core.persistence.record.DescendantResultRecord;
import org.kalibro.core.persistence.record.MetricResultRecord;
import org.kalibro.tests.UnitTest;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(MetricResultDatabaseDao.class)
public class MetricResultDatabaseDaoTest extends UnitTest {

	private static final Long ID = new Random().nextLong();
	private static final Long TIME = new Random().nextLong();
	private static final Date DATE = new Date(TIME);
	private static final String METRIC_NAME = "MetricResultDatabaseDaoTest metric name";

	private MetricResult metricResult;
	private MetricResultRecord record;

	private MetricResultDatabaseDao dao;

	@Before
	public void setUp() {
		metricResult = mock(MetricResult.class);
		record = mock(MetricResultRecord.class);
		when(record.convert()).thenReturn(metricResult);
		dao = spy(new MetricResultDatabaseDao());
	}

	@Test
	public void shouldGetDescendantResults() {
		DescendantResultRecord result = mock(DescendantResultRecord.class);
		TypedQuery<DescendantResultRecord> query = mock(TypedQuery.class);
		doReturn(query).when(dao).createQuery(
			"SELECT d FROM DescendantResult d WHERE d.metricResult.id = :metricResultId", DescendantResultRecord.class);
		when(query.getResultList()).thenReturn(list(result));
		when(result.convert()).thenReturn(42.0);

		assertDeepEquals(list(42.0), dao.descendantResultsOf(ID));
		verify(query).setParameter("metricResultId", ID);
	}

	@Test
	public void shouldGetMetricResultsOfModuleResult() {
		TypedQuery<MetricResultRecord> query = mock(TypedQuery.class);
		doReturn(query).when(dao).createRecordQuery("metricResult.moduleResult.id = :moduleResultId");
		when(query.getResultList()).thenReturn(list(record));

		assertDeepEquals(set(metricResult), dao.metricResultsOf(ID));
		verify(query).setParameter("moduleResultId", ID);
	}

	@Test
	public void shouldGetMetricResultHistory() {
		TypedQuery<Object[]> query = mock(TypedQuery.class);
		doReturn(query).when(dao).createQuery("SELECT p.date, mer FROM MetricResult mer " +
			"JOIN ModuleResult mor JOIN MetricConfigurationSnapshot mcs JOIN Processing p " +
			"WHERE mcs.metricName = :metricName AND mor.moduleName = " +
			"(SELECT moduleName FROM ModuleResult WHERE id = :moduleResultId)",
			Object[].class);
		List<Object[]> results = new ArrayList<Object[]>();
		results.add(new Object[]{TIME, record});
		when(query.getResultList()).thenReturn(results);

		SortedMap<Date, MetricResult> history = dao.historyOf(METRIC_NAME, ID);
		assertEquals(1, history.size());
		assertDeepEquals(metricResult, history.get(DATE));
		verify(query).setParameter("metricName", METRIC_NAME);
		verify(query).setParameter("moduleResultId", ID);
	}
}