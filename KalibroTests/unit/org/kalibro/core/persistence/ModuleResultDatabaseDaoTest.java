package org.kalibro.core.persistence;

import static org.junit.Assert.assertSame;

import java.util.Random;

import javax.persistence.TypedQuery;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.ModuleResult;
import org.kalibro.core.persistence.record.ModuleResultRecord;
import org.kalibro.tests.UnitTest;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ModuleResultDatabaseDao.class)
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
	public void shouldSave() {
		// TODO
	}

	@Test
	public void shouldPrepareResultForModule() {
		// TODO
	}

	private void prepareQuery(String clauses) {
		doReturn(query).when(dao).createRecordQuery(clauses);
	}
}