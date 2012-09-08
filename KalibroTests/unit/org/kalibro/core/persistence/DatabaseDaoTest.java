package org.kalibro.core.persistence;

import static org.junit.Assert.*;
import static org.kalibro.core.model.RangeFixtures.newRange;
import static org.kalibro.core.model.RangeLabel.BAD;
import static org.powermock.api.mockito.PowerMockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.TypedQuery;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.TestCase;
import org.kalibro.core.model.Range;
import org.kalibro.core.persistence.record.RangeRecord;
import org.mockito.Mockito;

public class DatabaseDaoTest extends TestCase {

	private RecordManager recordManager;

	private RangeDatabaseDao dao;

	@Before
	public void setUp() {
		recordManager = mock(RecordManager.class);
		dao = new RangeDatabaseDao(recordManager);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testGetAllNames() {
		String queryText = "SELECT x.name FROM \"Range\" x ORDER BY lower(x.name)";
		TypedQuery<String> query = mock(TypedQuery.class);
		List<String> names = Arrays.asList("4", "2");
		when(recordManager.createQuery(queryText, String.class)).thenReturn(query);
		when(query.getResultList()).thenReturn(names);

		assertSame(names, dao.getAllNames());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testConfirmEntity() {
		String queryText = "SELECT 1 FROM \"Range\" x WHERE x.name = :name";
		TypedQuery<String> query = mock(TypedQuery.class);
		when(recordManager.createQuery(queryText, String.class)).thenReturn(query);

		when(query.getResultList()).thenReturn(Arrays.asList("1"));
		assertTrue(dao.hasEntity("42"));
		Mockito.verify(query).setParameter("name", "42");

		when(query.getResultList()).thenReturn(new ArrayList<String>());
		assertFalse(dao.hasEntity("x"));
		Mockito.verify(query).setParameter("name", "x");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testGetByName() {
		String queryText = "SELECT x FROM \"Range\" x WHERE x.name = :name";
		TypedQuery<RangeRecord> query = mock(TypedQuery.class);
		Range range = newRange("amloc", BAD);
		when(recordManager.createQuery(queryText, RangeRecord.class)).thenReturn(query);
		when(query.getSingleResult()).thenReturn(new RangeRecord(range, null));

		assertDeepEquals(range, dao.getByName("42"));
		Mockito.verify(query).setParameter("name", "42");
	}

	private class RangeDatabaseDao extends DatabaseDao<Range, RangeRecord> {

		public RangeDatabaseDao(RecordManager recordManager) {
			super(recordManager, RangeRecord.class);
		}
	}
}