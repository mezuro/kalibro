package org.kalibro.core.persistence;

import javax.persistence.TypedQuery;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.dto.DataTransferObject;
import org.kalibro.tests.UnitTest;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DatabaseDaoFactory.class)
public abstract class DatabaseDaoTestCase<E, R extends DataTransferObject<E>, DAO extends DatabaseDao<E, R>> extends
	UnitTest {

	protected E entity;
	protected R record;
	protected TypedQuery<R> query;

	protected DAO dao;

	@Before
	public void setUp() throws Exception {
		mockStatic(DatabaseDaoFactory.class);
		createMocks();
		createDao();
	}

	private void createMocks() throws Exception {
		entity = mock(entityClass());
		mockRecord();
		query = mock(TypedQuery.class);
		when(query.getSingleResult()).thenReturn(record);
		when(query.getResultList()).thenReturn(list(record));
		when(record.convert()).thenReturn(entity);
	}

	private void mockRecord() throws Exception {
		record = mock(recordClass());
		try {
			recordClass().getDeclaredConstructor(entityClass(), Long.class);
			whenNew(recordClass()).withArguments(same(entity), anyLong()).thenReturn(record);
		} catch (Exception exception) {
			whenNew(recordClass()).withArguments(entity).thenReturn(record);
		}
	}

	private void createDao() throws Exception {
		dao = spy(daoClass().newInstance());
		doReturn(record).when(dao).save(record);
		doReturn(entity).when(dao).get(anyLong());
		doReturn(query).when(dao).createRecordQuery(anyString());
		doReturn(query).when(dao).createRecordQuery(anyString(), anyString());
	}

	private Class<E> entityClass() throws ClassNotFoundException {
		return (Class<E>) Class.forName("org.kalibro." + getClass().getSimpleName().replace("DatabaseDaoTest", ""));
	}

	private Class<R> recordClass() throws ClassNotFoundException {
		String packageName = "org.kalibro.core.persistence.record.";
		return (Class<R>) Class.forName(packageName + getClass().getSimpleName().replace("DatabaseDaoTest", "Record"));
	}

	private Class<DAO> daoClass() throws ClassNotFoundException {
		return (Class<DAO>) Class.forName(getClass().getName().replace("Test", ""));
	}

	@Test
	public void shouldGetRecordManager() {
		verifyStatic();
		DatabaseDaoFactory.createRecordManager();
	}
}