package org.kalibro.core.persistence;

import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.Random;

import javax.persistence.TypedQuery;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.Granularity;
import org.kalibro.Module;
import org.kalibro.ModuleResult;
import org.kalibro.core.persistence.record.ModuleResultRecord;
import org.kalibro.tests.UnitTest;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ModuleResult.class, ModuleResultDatabaseDao.class})
public class ModuleResultDatabaseDaoTest extends UnitTest {

	private static final Long ID = new Random().nextLong();

	private ModuleResult moduleResult;
	private ModuleResultRecord record;
	private TypedQuery<ModuleResultRecord> query;

	private ModuleResultDatabaseDao dao;

	@Before
	public void setUp() {
		moduleResult = mock(ModuleResult.class);
		record = mock(ModuleResultRecord.class);
		query = mock(TypedQuery.class);
		when(query.getSingleResult()).thenReturn(record);
		when(query.getResultList()).thenReturn(list(record));
		when(record.convert()).thenReturn(moduleResult);
		dao = spy(new ModuleResultDatabaseDao());
	}

	@Test
	public void shouldGetResultsRootOfProcessing() {
		prepareQuery("moduleResult.processing.id = :processingId AND moduleResult.parent = null");
		assertSame(moduleResult, dao.resultsRootOf(ID));
		verify(query).setParameter("processingId", ID);
	}

	@Test
	public void shouldGetParent() {
		prepareQuery("ModuleResult child JOIN child.parent moduleResult", "child.id = :childId");
		assertSame(moduleResult, dao.parentOf(ID));
		verify(query).setParameter("childId", ID);
	}

	@Test
	public void shouldGetChildren() {
		prepareQuery("ModuleResult parent ON parent.children moduleResult", "parent.id = :parentId");
		assertDeepEquals(set(moduleResult), dao.childrenOf(ID));
		verify(query).setParameter("parentId", ID);
	}

	@Test
	public void shouldSave() throws Exception {
		whenNew(ModuleResultRecord.class).withArguments(moduleResult, ID).thenReturn(record);
		doReturn(null).when(dao).save(record);
		dao.save(moduleResult, ID);
		verify(dao).save(record);
	}

	@Test
	public void shouldPrepareResultForModule() throws Exception {
		Module module = new Module(Granularity.PACKAGE, "org");
		String where = "moduleResult.processing.id = :processingId AND moduleResult.moduleName = :moduleName";
		doReturn(false).when(dao).exists("WHERE " + where, "processingId", ID, "moduleName", list("org"));
		doReturn(false).when(dao).exists("WHERE " + where, "processingId", ID, "moduleName", new ArrayList<String>());
		prepareQuery(where);

		Module preparedModule = mock(Module.class);
		whenNew(ModuleResultRecord.class)
			.withParameterTypes(Module.class, ModuleResultRecord.class, Long.class)
			.withArguments(any(Module.class), any(ModuleResultRecord.class), eq(ID)).thenReturn(record);
		doReturn(null).when(dao).save(record);
		when(moduleResult.getModule()).thenReturn(preparedModule);

		assertSame(moduleResult, dao.prepareResultFor(module, ID));
		verifyCallsInOrder(preparedModule);
	}

	private void prepareQuery(String where) {
		doReturn(query).when(dao).createRecordQuery(where);
	}

	private void prepareQuery(String from, String where) {
		doReturn(query).when(dao).createRecordQuery(from, where);
	}

	private void verifyCallsInOrder(Module preparedModule) {
		InOrder order = Mockito.inOrder(dao, query, dao, query, preparedModule);
		order.verify(dao).save(record);
		order.verify(query).setParameter("processingId", ID);
		order.verify(query).setParameter("moduleName", new ArrayList<String>());
		order.verify(query).getSingleResult();
		order.verify(dao).save(record);
		order.verify(query).setParameter("processingId", ID);
		order.verify(query).setParameter("moduleName", list("org"));
		order.verify(query).getSingleResult();
		order.verify(preparedModule).setGranularity(Granularity.PACKAGE);
	}
}