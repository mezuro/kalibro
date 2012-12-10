package org.kalibro.core.persistence;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Random;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.core.concurrent.VoidTask;
import org.kalibro.core.persistence.PersonDatabaseDao.Person;
import org.kalibro.core.persistence.PersonDatabaseDao.PersonRecord;
import org.kalibro.tests.UnitTest;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DatabaseDaoFactory.class)
public class DatabaseDaoTest extends UnitTest {

	private static final Long ID = Math.abs(new Random().nextLong());

	private Person person;
	private PersonRecord record;
	private RecordManager recordManager;

	private PersonDatabaseDao dao;

	@Before
	public void setUp() {
		person = mock(Person.class);
		record = mock(PersonRecord.class);
		recordManager = mock(RecordManager.class);
		mockStatic(DatabaseDaoFactory.class);
		when(DatabaseDaoFactory.createRecordManager()).thenReturn(recordManager);
		when(record.convert()).thenReturn(person);

		dao = new PersonDatabaseDao();
	}

	@Test
	public void shouldConfirmExistence() {
		Query query = mock(Query.class);
		when(recordManager.createQuery("SELECT 1 FROM Person person WHERE person.id = :id")).thenReturn(query);

		when(query.getResultList()).thenReturn(list(1));
		assertTrue(dao.exists(ID));
		verify(query).setParameter("id", ID);

		when(query.getResultList()).thenReturn(new ArrayList<Integer>());
		assertFalse(dao.exists(-ID));
		verify(query).setParameter("id", -ID);
	}

	@Test
	public void shouldConfirmExistenceWithClause() {
		Query query = mock(Query.class);
		String clause = "WHERE person.parent.id = :parentId";
		when(recordManager.createQuery("SELECT 1 FROM Person person " + clause)).thenReturn(query);

		when(query.getResultList()).thenReturn(list(1));
		assertTrue(dao.exists(clause, "parentId", ID));
		verify(query).setParameter("parentId", ID);

		when(query.getResultList()).thenReturn(new ArrayList<Integer>());
		assertFalse(dao.exists(clause, "parentId", -ID));
		verify(query).setParameter("parentId", -ID);
	}

	@Test
	public void shouldGetById() {
		when(recordManager.getById(ID, PersonRecord.class)).thenReturn(record);
		assertSame(person, dao.get(ID));
	}

	@Test
	public void shouldThowExceptionWhenGettingWithInvalidId() {
		when(recordManager.getById(ID, PersonRecord.class)).thenReturn(null);
		assertThat(new VoidTask() {

			@Override
			protected void perform() throws Throwable {
				dao.get(ID);
			}
		}).throwsException().withMessage("Person " + ID + " not found.");
	}

	@Test
	public void shouldGetAll() {
		TypedQuery<PersonRecord> query = mock(TypedQuery.class);
		when(recordManager.createQuery("SELECT person FROM Person person", PersonRecord.class)).thenReturn(query);
		when(query.getResultList()).thenReturn(list(record));
		assertDeepEquals(set(person), dao.all());
	}

	@Test
	public void shouldCreateRecordQueryWithClauses() {
		String from = "Person parent JOIN parent.children child";
		String where = "child.id = :id";
		TypedQuery<PersonRecord> query = mock(TypedQuery.class);
		when(recordManager.createQuery("SELECT person FROM " + from + " WHERE " + where, PersonRecord.class))
			.thenReturn(query);
		assertSame(query, dao.createRecordQuery(from, where));
	}

	@Test
	public void shouldSave() {
		when(recordManager.save(record)).thenReturn(record);
		assertSame(record, dao.save(record));
	}

	@Test
	public void shouldDeleteById() {
		dao.delete(ID);
		verify(recordManager).removeById(ID, PersonRecord.class);
	}
}