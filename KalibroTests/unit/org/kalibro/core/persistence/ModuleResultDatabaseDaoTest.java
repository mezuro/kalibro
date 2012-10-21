package org.kalibro.core.persistence;

import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.Random;

import javax.persistence.TypedQuery;

import org.junit.Before;
import org.junit.Test;
import org.junit.rules.Timeout;
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
		when(query.getResultList()).thenReturn(asList(record));
		when(record.convert()).thenReturn(moduleResult);
		dao = spy(new ModuleResultDatabaseDao(null));
	}

	@Test
	public void shouldGetResultsRootOfProcessing() {
		prepareQuery("WHERE moduleResult.processing.id = :processingId AND moduleResult.parent = null");
		assertSame(moduleResult, dao.resultsRootOf(ID));
		verify(query).setParameter("processingId", ID);
	}

	@Test
	public void shouldGetParent() {
		prepareQuery("JOIN ModuleResult child ON child.parent = moduleResult WHERE child.id = :childId");
		assertSame(moduleResult, dao.parentOf(ID));
		verify(query).setParameter("childId", ID);
	}

	@Test
	public void shouldGetChildren() {
		prepareQuery("JOIN ModuleResult parent ON moduleResult.parent = parent WHERE parent.id = :parentId");
		assertDeepEquals(asSet(moduleResult), dao.childrenOf(ID));
		verify(query).setParameter("parentId", ID);
	}

	@Test
	public void shouldSave() throws Exception {
		whenNew(ModuleResultRecord.class).withArguments(moduleResult, ID).thenReturn(record);
		doReturn(null).when(dao).save(record);
		dao.save(moduleResult, ID);
		verify(dao).save(record);
	}

	@Override
	protected Timeout testTimeout() {
		return new Timeout(0);
	}

	@Test
	public void shouldPrepareResultForModule() throws Exception {
		Module module = new Module(Granularity.PACKAGE, "org");
		String clause = "WHERE moduleResult.processing.id = :processingId AND moduleResult.moduleName = :moduleName";
		doReturn(false).when(dao).exists(clause, "processingId", ID, "moduleName", asList("org"));
		doReturn(false).when(dao).exists(clause, "processingId", ID, "moduleName", new ArrayList<String>());
		prepareQuery(clause);

		Module preparedModule = mock(Module.class);
		whenNew(ModuleResultRecord.class)
			.withParameterTypes(Module.class, ModuleResultRecord.class, Long.class)
			.withArguments(any(Module.class), any(ModuleResultRecord.class), eq(ID)).thenReturn(record);
		doReturn(null).when(dao).save(record);
		when(moduleResult.getModule()).thenReturn(preparedModule);

		assertSame(moduleResult, dao.prepareResultFor(module, ID));
		verifyCallsInOrder(preparedModule);
	}

	private void prepareQuery(String clauses) {
		doReturn(query).when(dao).createRecordQuery(clauses);
	}

	private void verifyCallsInOrder(Module preparedModule) {
		InOrder order = Mockito.inOrder(dao, query, dao, query, preparedModule);
		order.verify(dao).save(record);
		order.verify(query).setParameter("processingId", ID);
		order.verify(query).setParameter("moduleName", new ArrayList<String>());
		order.verify(query).getSingleResult();
		order.verify(dao).save(record);
		order.verify(query).setParameter("processingId", ID);
		order.verify(query).setParameter("moduleName", asList("org"));
		order.verify(query).getSingleResult();
		order.verify(preparedModule).setGranularity(Granularity.PACKAGE);
	}
}