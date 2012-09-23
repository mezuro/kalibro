package org.kalibro.core.persistence;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import javax.persistence.TypedQuery;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.Reading;
import org.kalibro.core.persistence.record.ReadingRecord;
import org.kalibro.tests.UnitTest;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ReadingDatabaseDao.class)
public class ReadingDatabaseDaoTest extends UnitTest {

	private Reading reading;
	private ReadingRecord record;

	private ReadingDatabaseDao dao;

	@Before
	public void setUp() {
		reading = mock(Reading.class);
		record = mock(ReadingRecord.class);
		when(record.convert()).thenReturn(reading);
		dao = spy(new ReadingDatabaseDao(null));
	}

	@Test
	public void shouldGetReadingsOfGroup() {
		TypedQuery<ReadingRecord> query = mock(TypedQuery.class);
		doReturn(query).when(dao).createRecordQuery("WHERE reading.group.id = :groupId ORDER BY reading.grade");
		when(query.getResultList()).thenReturn(Arrays.asList(record));

		assertDeepList(dao.readingsOf(42L), reading);
		verify(query).setParameter("groupId", 42L);
	}

	@Test
	public void shouldSave() throws Exception {
		when(reading.getGroupId()).thenReturn(28L);
		whenNew(ReadingRecord.class).withArguments(reading, 28L).thenReturn(record);
		doReturn(record).when(dao).save(record);
		when(record.id()).thenReturn(42L);

		assertEquals(42L, dao.save(reading).longValue());
		verify(dao).save(record);
	}

	@Test
	public void shouldDelete() {
		doNothing().when(dao).deleteById(42L);
		dao.delete(42L);
		verify(dao).deleteById(42L);
	}
}