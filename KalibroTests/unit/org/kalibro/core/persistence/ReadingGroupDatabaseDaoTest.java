package org.kalibro.core.persistence;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.ReadingGroup;
import org.kalibro.TestCase;
import org.kalibro.core.persistence.record.ReadingGroupRecord;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ReadingGroupDatabaseDao.class)
public class ReadingGroupDatabaseDaoTest extends TestCase {

	private ReadingGroupDatabaseDao dao;

	@Before
	public void setUp() {
		dao = spy(new ReadingGroupDatabaseDao(null));
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
		assertSame(group, dao.get(42L));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSave() throws Exception {
		ReadingGroup group = mock(ReadingGroup.class);
		ReadingGroupRecord record = mock(ReadingGroupRecord.class);
		whenNew(ReadingGroupRecord.class).withArguments(group).thenReturn(record);
		doReturn(record).when(dao).save(record);
		when(record.id()).thenReturn(42L);

		assertEquals(42L, dao.save(group).longValue());
		verify(dao).save(record);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldDelete() {
		doNothing().when(dao).deleteById(42L);
		dao.delete(42L);
		verify(dao).deleteById(42L);
	}
}