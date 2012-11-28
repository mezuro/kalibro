package org.kalibro.core.persistence;

import static org.junit.Assert.*;

import java.util.*;

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

	@Test
	public void shouldGetChildren() {
		assertDeepEquals(set(entity), dao.childrenOf(ID));

		verify(dao).createRecordQuery("ModuleResult parent JOIN parent.children moduleResult", "parent.id = :parentId");
		verify(query).setParameter("parentId", ID);
	}

	@Test
	public void shouldGetHistory() {
		TypedQuery<Object[]> historyQuery = mock(TypedQuery.class);
		doReturn(historyQuery).when(dao).createQuery("SELECT processing.date, result FROM ModuleResult result " +
			"JOIN result.processing processing WHERE result.moduleName = :moduleName AND processing.repository.id = "+
			"(SELECT mor.processing.repository.id FROM ModuleResult mor WHERE mor.id = :resultId)", Object[].class);
		
		long time = new Random().nextLong();
		Date date = new Date(time);
		List<Object[]> results = new ArrayList<Object[]>();
		results.add(new Object[]{time, record});
		when(historyQuery.getResultList()).thenReturn(results);

		SortedMap<Date, ModuleResult> history = dao.historyOf(ID);
		assertEquals(1, history.size());
		assertDeepEquals(entity, history.get(date));
		verify(historyQuery).setParameter("moduleName", entity.getModule().getName());
		verify(historyQuery).setParameter("resultId", entity.getId());
	}
	
	@Test
	public void shouldSave() throws Exception {
		dao.save(entity, ID);

		verifyNew(ModuleResultRecord.class).withArguments(entity, ID);
		verify(dao).save(record);
	}

	@Test
	public void shouldPrepareResultForModule() throws Exception {
		Module module = new Module(Granularity.PACKAGE, "org");
		String where = "moduleResult.processing.id = :processingId AND moduleResult.moduleName = :module";
		String where2 = "moduleResult.processing.id = :processingId AND moduleResult.moduleGranularity = :module";
		doReturn(false).when(dao).exists("WHERE " + where, "processingId", ID, "module", list("org"));
		doReturn(false).when(dao).exists("WHERE " + where2, "processingId", ID, "module", "SOFTWARE");
		prepareQuery(where);
		prepareQuery(where2);

		Module preparedModule = mock(Module.class);
		whenNew(ModuleResultRecord.class)
			.withParameterTypes(Module.class, ModuleResultRecord.class, Long.class)
			.withArguments(any(Module.class), any(ModuleResultRecord.class), eq(ID)).thenReturn(record);
		doReturn(null).when(dao).save(record);
		when(entity.getModule()).thenReturn(preparedModule);

		assertSame(entity, dao.prepareResultFor(module, ID));
		verifyCallsInOrder(preparedModule);
	}

	private void prepareQuery(String where) {
		doReturn(query).when(dao).createRecordQuery(where);
	}

	private void verifyCallsInOrder(Module preparedModule) {
		InOrder order = Mockito.inOrder(dao, query, dao, query, preparedModule);
		order.verify(dao).save(record);
		order.verify(query).setParameter("processingId", ID);
		order.verify(query).setParameter("module", "SOFTWARE");
		order.verify(query).getSingleResult();
		order.verify(dao).save(record);
		order.verify(query).setParameter("processingId", ID);
		order.verify(query).setParameter("module", list("org"));
		order.verify(query).getSingleResult();
		order.verify(preparedModule).setGranularity(Granularity.PACKAGE);
	}
}