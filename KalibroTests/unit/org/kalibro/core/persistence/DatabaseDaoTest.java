package org.kalibro.core.persistence;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.TestCase;
import org.kalibro.core.persistence.PersonDatabaseDao.Person;
import org.kalibro.core.persistence.PersonDatabaseDao.PersonRecord;

public class DatabaseDaoTest extends TestCase {

	private Person person;
	private PersonRecord record;
	private RecordManager recordManager;

	private PersonDatabaseDao dao;

	@Before
	public void setUp() {
		person = mock(Person.class);
		record = mock(PersonRecord.class);
		recordManager = mock(RecordManager.class);
		when(record.convert()).thenReturn(person);

		dao = new PersonDatabaseDao(recordManager);
	}

	@Test
	public void shouldConfirmExistence() {
		Query query = mock(Query.class);
		when(recordManager.createQuery("SELECT 1 FROM Person WHERE id = :id")).thenReturn(query);

		when(query.getResultList()).thenReturn(Arrays.asList(1));
		assertTrue(dao.existsWithId(42L));
		verify(query).setParameter("id", 42L);

		when(query.getResultList()).thenReturn(new ArrayList<Integer>());
		assertFalse(dao.existsWithId(28L));
		verify(query).setParameter("id", 28L);
	}

	@Test
	public void shouldGetById() {
		when(recordManager.getById(42L, PersonRecord.class)).thenReturn(record);
		assertSame(person, dao.getById(42L));
	}

	@Test
	public void shouldGetAllOrderedByName() {
		TypedQuery<PersonRecord> query = mock(TypedQuery.class);
		String queryString = "SELECT person FROM Person person ORDER BY lower(person.name)";
		when(recordManager.createQuery(queryString, PersonRecord.class)).thenReturn(query);
		when(query.getResultList()).thenReturn(Arrays.asList(record));

		assertDeepList(dao.allOrderedByName(), person);
	}

	@Test
	public void shouldSave() {
		when(recordManager.save(record)).thenReturn(record);
		assertSame(record, dao.save(record));
	}

	@Test
	public void shouldDeleteById() {
		Query query = mock(Query.class);
		String queryString = "DELETE FROM Person WHERE id = :id";
		when(recordManager.createQuery(queryString)).thenReturn(query);

		dao.deleteById(42L);
		verify(recordManager).executeUpdate(query);
	}
}