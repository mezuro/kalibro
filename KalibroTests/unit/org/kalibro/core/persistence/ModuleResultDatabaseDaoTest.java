package org.kalibro.core.persistence;

import static org.junit.Assert.*;

import java.util.*;

import javax.persistence.StoredProcedureQuery;
import javax.persistence.TypedQuery;

import org.junit.Test;
import org.kalibro.Granularity;
import org.kalibro.Module;
import org.kalibro.ModuleResult;
import org.kalibro.core.persistence.record.ModuleResultRecord;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

@PrepareForTest({ModuleResult.class, ModuleResultDatabaseDao.class})
public class ModuleResultDatabaseDaoTest extends
	DatabaseDaoTestCase<ModuleResult, ModuleResultRecord, ModuleResultDatabaseDao> {

	private static final Long ID = new Random().nextLong();
	private static final Long TIME = new Random().nextLong();
	private static final Date DATE = new Date(TIME);

	@Test
	public void shouldGetChildren() {
		assertDeepEquals(set(entity), dao.childrenOf(ID));

		verify(dao).createRecordQuery("moduleResult.parent = :parentId");
		verify(query).setParameter("parentId", ID);
	}

	@Test
	public void shouldGetHistory() {
		Module module = mockModule();

		TypedQuery<Object[]> historyQuery = mock(TypedQuery.class);
		doReturn(historyQuery).when(dao).createQuery(anyString(), eq(Object[].class));

		List<Object[]> results = new ArrayList<Object[]>();
		results.add(new Object[]{TIME, record});
		when(historyQuery.getResultList()).thenReturn(results);

		SortedMap<Date, ModuleResult> history = dao.historyOf(ID);
		assertEquals(1, history.size());
		assertDeepEquals(entity, history.get(DATE));
		verify(historyQuery).setParameter("moduleName", module.getName()[0]);
		verify(historyQuery).setParameter("moduleResultId", ID);
	}

	private Module mockModule() {
		dao = mock(ModuleResultDatabaseDao.class, Mockito.CALLS_REAL_METHODS);
		Module module = new Module(Granularity.CLASS, "ModuleResultDatabaseDaoTest");
		doReturn(entity).when(dao).get(ID);
		when(entity.getModule()).thenReturn(module);
		return module;
	}

	@Test
	public void shouldGetResultForModule() {
		Module module = new Module(Granularity.PACKAGE, "org");
		prepareQuery("moduleResult.moduleName = :module");

		assertSame(entity, dao.getResultFor(module, ID));
		verifyCallsInOrder("module", "org");
	}

	@Test
	public void shouldGetNullForInexistentResult() {
		Module module = new Module(Granularity.PACKAGE, "org");
		prepareQuery("moduleResult.moduleName = :module");
		when(query.getResultList()).thenReturn(new ArrayList<ModuleResultRecord>());

		assertNull(dao.getResultFor(module, ID));
		verifyCallsInOrder("module", "org");
	}

	@Test
	public void shouldFindResultByGranularityWhenRoot() {
		Module module = new Module(Granularity.SOFTWARE, "null");
		prepareQuery("moduleResult.moduleGranularity = :module");

		assertSame(entity, dao.getResultFor(module, ID));
		verifyCallsInOrder("module", "SOFTWARE");
	}

	@Test
	public void shouldSave() throws Exception {
		assertSame(entity, dao.save(entity, ID));
		verifyNew(ModuleResultRecord.class).withArguments(entity, ID);
		verify(dao).save(record);
	}

	@Test
	public void shouldAggregateResults() {
		StoredProcedureQuery procedure = mock(StoredProcedureQuery.class);
		doReturn(procedure).when(dao).createProcedureQuery("aggregate_results");
		dao.aggregateResults(ID);
		InOrder order = Mockito.inOrder(procedure);
		order.verify(procedure).setParameter("process_id", ID);
		order.verify(procedure).execute();
	}

	@Test
	public void shouldGetResultsOfProcessing() {
		prepareQuery(null);
		assertDeepEquals(list(entity), dao.getResultsOfProcessing(ID));
		verifyCallsInOrder(null, null);
	}

	private void prepareQuery(String secondCondition) {
		String where = "moduleResult.processing = :processingId";
		if (secondCondition != null)
			where += " AND " + secondCondition;
		doReturn(query).when(dao).createRecordQuery(where);
	}

	private void verifyCallsInOrder(String secondParameterName, Object secondParameter) {
		InOrder order = Mockito.inOrder(query);
		order.verify(query).setParameter("processingId", ID);
		if (secondParameterName != null)
			order.verify(query).setParameter(secondParameterName, secondParameter);
		order.verify(query).getResultList();
	}
}