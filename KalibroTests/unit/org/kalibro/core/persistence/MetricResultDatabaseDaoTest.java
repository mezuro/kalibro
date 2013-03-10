package org.kalibro.core.persistence;

import static org.junit.Assert.*;

import java.util.*;

import javax.persistence.TypedQuery;

import org.junit.Test;
import org.kalibro.Granularity;
import org.kalibro.MetricResult;
import org.kalibro.Module;
import org.kalibro.ModuleResult;
import org.kalibro.core.persistence.record.DescendantResultRecord;
import org.kalibro.core.persistence.record.MetricResultRecord;
import org.powermock.core.classloader.annotations.PrepareForTest;

@PrepareForTest({MetricResultDatabaseDao.class, ModuleResult.class})
public class MetricResultDatabaseDaoTest extends
	DatabaseDaoTestCase<MetricResult, MetricResultRecord, MetricResultDatabaseDao> {

	private static final Random RANDOM = new Random();

	private static final Long MODULE_RESULT_ID = RANDOM.nextLong();
	private static final Long METRIC_RESULT_ID = RANDOM.nextLong();
	private static final Long CONFIGURATION_ID = RANDOM.nextLong();

	private static final Long TIME = RANDOM.nextLong();
	private static final Date DATE = new Date(TIME);
	private static final String METRIC_NAME = "MetricResultDatabaseDaoTest metric name";

	@Test
	public void shouldGetDescendantResultsByMetricResultId() {
		Double value = RANDOM.nextDouble();
		DescendantResultRecord result = mock(DescendantResultRecord.class);
		TypedQuery<DescendantResultRecord> descendantQuery = mock(TypedQuery.class);
		doReturn(descendantQuery).when(dao).createQuery(anyString(), eq(DescendantResultRecord.class));
		when(descendantQuery.getResultList()).thenReturn(list(result));
		when(result.convert()).thenReturn(value);

		assertDeepEquals(list(value), dao.descendantResultsOf(METRIC_RESULT_ID));
		verify(descendantQuery).setParameter("metricResultId", METRIC_RESULT_ID);
	}

	@Test
	public void shouldGetMetricResultsOfModuleResult() {
		assertDeepEquals(set(entity), dao.metricResultsOf(MODULE_RESULT_ID));

		verify(dao).createRecordQuery("metricResult.moduleResult = :moduleResultId");
		verify(query).setParameter("moduleResultId", MODULE_RESULT_ID);
	}

	@Test
	public void shouldGetMetricResultHistory() throws Exception {
		mockModuleResult("org");
		TypedQuery<Object[]> historyQuery = mock(TypedQuery.class);
		doReturn(historyQuery).when(dao).createQuery(anyString(), eq(Object[].class));
		List<Object[]> results = new ArrayList<Object[]>();
		results.add(new Object[]{TIME, record});
		when(historyQuery.getResultList()).thenReturn(results);

		SortedMap<Date, MetricResult> history = dao.historyOf(METRIC_NAME, MODULE_RESULT_ID);
		assertEquals(1, history.size());
		assertDeepEquals(entity, history.get(DATE));
		verify(historyQuery).setParameter("metricName", METRIC_NAME);
		verify(historyQuery).setParameter("moduleName", "org");
		verify(historyQuery).setParameter("moduleResultId", MODULE_RESULT_ID);
	}

	private void mockModuleResult(String moduleName) throws Exception {
		Module module = new Module(Granularity.PACKAGE, moduleName);
		ModuleResult moduleResult = mock(ModuleResult.class);
		ModuleResultDatabaseDao moduleResultDao = mock(ModuleResultDatabaseDao.class);
		whenNew(ModuleResultDatabaseDao.class).withNoArguments().thenReturn(moduleResultDao);
		when(moduleResultDao.get(MODULE_RESULT_ID)).thenReturn(moduleResult);
		when(moduleResult.getModule()).thenReturn(module);
	}

	@Test
	public void shouldSave() throws Exception {
		assertSame(entity, dao.save(entity, MODULE_RESULT_ID));
		verifyNew(MetricResultRecord.class).withArguments(entity, MODULE_RESULT_ID);
		verify(dao).save(record);
	}

	@Test
	public void shouldCheckExistenceOfMetricResult() {
		Random random = new Random();
		Long moduleResultId = random.nextLong();
		Long configurationId = random.nextLong();
		TypedQuery<Long> countQuery = mock(TypedQuery.class);
		doReturn(countQuery).when(dao).createQuery(anyString(), eq(Long.class));

		when(countQuery.getSingleResult()).thenReturn(1L);
		assertTrue(dao.metricResultExists(moduleResultId, configurationId));

		when(countQuery.getSingleResult()).thenReturn(0L);
		assertFalse(dao.metricResultExists(moduleResultId, configurationId));
	}

	@Test
	public void shouldGetDescendantResultsByModuleAndConfiguration() {
		Double value = RANDOM.nextDouble();
		DescendantResultRecord result = mock(DescendantResultRecord.class);
		TypedQuery<DescendantResultRecord> descendantQuery = mock(TypedQuery.class);
		doReturn(descendantQuery).when(dao).createQuery(anyString(), eq(DescendantResultRecord.class));
		when(descendantQuery.getResultList()).thenReturn(list(result));
		when(result.convert()).thenReturn(value);

		assertDeepEquals(list(value), dao.descendantResultsOf(MODULE_RESULT_ID, CONFIGURATION_ID));
		verify(descendantQuery).setParameter("moduleResultId", MODULE_RESULT_ID);
		verify(descendantQuery).setParameter("configurationId", CONFIGURATION_ID);
	}

	@Test
	public void shouldInsertDescendantResults() throws Exception {
		Double value = RANDOM.nextDouble();
		DescendantResultRecord result = mock(DescendantResultRecord.class);
		whenNew(DescendantResultRecord.class)
			.withArguments(value, MODULE_RESULT_ID, CONFIGURATION_ID).thenReturn(result);
		doNothing().when(dao).saveAll(list(result));

		dao.addDescendantResults(list(value), MODULE_RESULT_ID, CONFIGURATION_ID);
		verifyNew(DescendantResultRecord.class).withArguments(value, MODULE_RESULT_ID, CONFIGURATION_ID);
		verify(dao).saveAll(list(result));
	}
}