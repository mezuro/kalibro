package org.kalibro.core.persistence;

import static org.junit.Assert.assertSame;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.Reading;
import org.kalibro.ReadingGroup;
import org.kalibro.TestCase;
import org.kalibro.core.persistence.record.ReadingGroupRecord;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ReadingGroupDatabaseDao.class)
public class ReadingGroupDatabaseDaoTest extends TestCase {

	private RecordManager recordManager;
	private ReadingGroupDatabaseDao dao;

	@Before
	public void setUp() {
		recordManager = mock(RecordManager.class);
		dao = spy(new ReadingGroupDatabaseDao(recordManager));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldGetAll() {
		List<ReadingGroup> all = mock(List.class);
		doReturn(all).when(dao).allOrderedByName();
		assertSame(all, dao.all());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldGetById() {
		ReadingGroup group = mock(ReadingGroup.class);
		doReturn(group).when(dao).getById(42L);
		assertSame(group, dao.getById(42L));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSaveAndMerge() throws Exception {
		List<Reading> readings = mock(List.class);
		ReadingGroup group = mock(ReadingGroup.class);
		ReadingGroupRecord record = mock(ReadingGroupRecord.class);
		whenNew(ReadingGroupRecord.class).withArguments(group).thenReturn(record);
		when(recordManager.save(record)).thenReturn(record);
		when(record.convert()).thenReturn(group);
		when(group.getId()).thenReturn(42L);
		when(group.getReadings()).thenReturn(readings);

		dao.save(group);
		verify(group).setId(42L);
		verify(group).setReadings(readings);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldDelete() {
		doNothing().when(dao).deleteById(42L);
		dao.delete(42L);
		verify(dao).deleteById(42L);
	}
}