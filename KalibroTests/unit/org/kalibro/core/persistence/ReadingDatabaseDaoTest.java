package org.kalibro.core.persistence;

import java.util.Arrays;

import javax.persistence.TypedQuery;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.Reading;
import org.kalibro.TestCase;
import org.kalibro.core.persistence.record.ReadingRecord;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ReadingDatabaseDao.class)
public class ReadingDatabaseDaoTest extends TestCase {

	private Reading reading;
	private ReadingRecord record;

	private RecordManager recordManager;
	private ReadingDatabaseDao dao;

	@Before
	public void setUp() {
		recordManager = mock(RecordManager.class);
		dao = spy(new ReadingDatabaseDao(recordManager));

		reading = mock(Reading.class);
		record = mock(ReadingRecord.class);
		when(record.convert()).thenReturn(reading);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldGetReadingsOfGroup() {
		TypedQuery<ReadingRecord> query = mock(TypedQuery.class);
		String queryString = "SELECT reading FROM Reading reading WHERE reading.group.id = :groupId";
		doReturn(query).when(dao).createRecordQuery(queryString);
		when(query.getResultList()).thenReturn(Arrays.asList(record));

		assertDeepList(dao.readingsOf(42L), reading);
		verify(query).setParameter("groupId", 42L);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSaveAndMerge() throws Exception {
		when(reading.getGroupId()).thenReturn(28L);
		whenNew(ReadingRecord.class).withArguments(reading, 28L).thenReturn(record);
		when(recordManager.save(record)).thenReturn(record);
		when(reading.getId()).thenReturn(42L);

		dao.save(reading);
		verify(reading).setId(42L);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldDelete() {
		doNothing().when(dao).deleteById(42L);
		dao.delete(42L);
		verify(dao).deleteById(42L);
	}
}