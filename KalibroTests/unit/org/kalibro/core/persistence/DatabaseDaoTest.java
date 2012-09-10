package org.kalibro.core.persistence;

import static org.junit.Assert.assertSame;

import java.util.Arrays;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.TestCase;
import org.kalibro.core.persistence.PersonDatabaseDao.Person;
import org.kalibro.core.persistence.PersonDatabaseDao.PersonRecord;
import org.mockito.InOrder;
import org.mockito.Mockito;

public class DatabaseDaoTest extends TestCase {

	private RecordManager recordManager;

	private PersonDatabaseDao dao;

	@Before
	public void setUp() {
		recordManager = mock(RecordManager.class);
		dao = new PersonDatabaseDao(recordManager);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldGetAllOrderedByName() {
		TypedQuery<PersonRecord> query = mock(TypedQuery.class);
		String queryString = "SELECT person FROM Person person ORDER BY lower(person.name)";
		when(recordManager.createQuery(queryString, PersonRecord.class)).thenReturn(query);

		Person person = mock(Person.class);
		PersonRecord record = mock(PersonRecord.class);
		List<PersonRecord> records = Arrays.asList(record);
		when(query.getResultList()).thenReturn(records);
		when(record.convert()).thenReturn(person);

		assertDeepList(dao.allOrderedByName(), person);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldGetById() {
		Person person = mock(Person.class);
		PersonRecord record = mock(PersonRecord.class);
		when(recordManager.getById(42L, PersonRecord.class)).thenReturn(record);
		when(record.convert()).thenReturn(person);

		assertSame(person, dao.getById(42L));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldDeleteById() {
		Query query = mock(Query.class);
		String queryString = "DELETE FROM Person WHERE id = :id";
		when(recordManager.createQuery(queryString)).thenReturn(query);

		dao.deleteById(42L);
		verify(query).setParameter("id", 42L);
		InOrder order = Mockito.inOrder(recordManager, query, recordManager);
		order.verify(recordManager).beginTransaction();
		order.verify(query).executeUpdate();
		order.verify(recordManager).commitTransaction();
	}
}