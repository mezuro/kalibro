package org.kalibro.core.persistence.database;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.model.Range;
import org.kalibro.core.model.RangeFixtures;
import org.kalibro.core.model.RangeLabel;
import org.kalibro.core.persistence.database.entities.RangeRecord;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

public class DatabaseDaoTest extends KalibroTestCase {

	private DatabaseManager databaseManager;

	private RangeDatabaseDao dao;

	@Before
	public void setUp() {
		databaseManager = PowerMockito.mock(DatabaseManager.class);
		dao = new RangeDatabaseDao(databaseManager);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testGetAllNames() {
		String queryText = "SELECT x.name FROM \"Range\" x ORDER BY x.name";
		Query<String> query = PowerMockito.mock(Query.class);
		List<String> names = Arrays.asList("4", "2");
		PowerMockito.when(databaseManager.createQuery(queryText, String.class)).thenReturn(query);
		PowerMockito.when(query.getResultList()).thenReturn(names);

		assertSame(names, dao.getAllNames());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testGetByName() {
		String queryText = "SELECT x FROM \"Range\" x WHERE x.name = :name";
		Query<RangeRecord> query = PowerMockito.mock(Query.class);
		Range range = RangeFixtures.amlocRange(RangeLabel.BAD);
		String noResultMessage = "There is no range named '42'";
		PowerMockito.when(databaseManager.createQuery(queryText, RangeRecord.class)).thenReturn(query);
		PowerMockito.when(query.getSingleResult(noResultMessage)).thenReturn(new RangeRecord(range, null));

		assertDeepEquals(range, dao.getByName("42"));
		Mockito.verify(query).setParameter("name", "42");
	}

	private class RangeDatabaseDao extends DatabaseDao<Range, RangeRecord> {

		public RangeDatabaseDao(DatabaseManager databaseManager) {
			super(databaseManager, RangeRecord.class);
		}
	}
}