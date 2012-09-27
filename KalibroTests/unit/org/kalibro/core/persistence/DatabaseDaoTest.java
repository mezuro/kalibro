package org.kalibro.core.persistence;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Random;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.core.persistence.PersonDatabaseDao.Person;
import org.kalibro.core.persistence.PersonDatabaseDao.PersonRecord;
import org.kalibro.tests.UnitTest;

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
		when(record.convert()).thenReturn(person);

		dao = new PersonDatabaseDao(recordManager);
	}

	@Test
	public void shouldConfirmExistence() {
		Query query = mock(Query.class);
		when(recordManager.createQuery("SELECT 1 FROM Person WHERE id = :id")).thenReturn(query);

		when(query.getResultList()).thenReturn(asList(1));
		assertTrue(dao.exists(ID));
		verify(query).setParameter("id", ID);

		when(query.getResultList()).thenReturn(new ArrayList<Integer>());
		assertFalse(dao.exists(-1L));
		verify(query).setParameter("id", -1L);
	}

	@Test
	public void shouldGetById() {
		when(recordManager.getById(ID, PersonRecord.class)).thenReturn(record);
		assertSame(person, dao.get(ID));
	}

	@Test
	public void shouldGetAll() {
		TypedQuery<PersonRecord> query = mock(TypedQuery.class);
		when(recordManager.createQuery("SELECT person FROM Person person ", PersonRecord.class)).thenReturn(query);
		when(query.getResultList()).thenReturn(asList(record));
		assertDeepEquals(asSet(person), dao.all());
	}

	@Test
	public void shouldSave() {
		when(recordManager.save(record)).thenReturn(record);
		assertSame(record, dao.save(record));
	}

	@Test
	public void shouldDeleteById() {
		Query query = mock(Query.class);
		when(recordManager.createQuery("DELETE FROM Person WHERE id = :id")).thenReturn(query);

		dao.delete(ID);
		verify(recordManager).executeUpdate(query);
	}
}