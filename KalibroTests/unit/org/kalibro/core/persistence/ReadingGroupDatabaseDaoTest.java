package org.kalibro.core.persistence;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.ReadingGroup;
import org.kalibro.core.persistence.record.ReadingGroupRecord;
import org.kalibro.tests.UnitTest;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ReadingGroupDatabaseDao.class)
public class ReadingGroupDatabaseDaoTest extends UnitTest {

	private static final Long ID = Math.abs(new Random().nextLong());

	private ReadingGroupDatabaseDao dao;

	@Before
	public void setUp() {
		dao = spy(new ReadingGroupDatabaseDao(null));
	}

	@Test
	public void shouldConfirmExistence() {
		doReturn(false).when(dao).existsWithId(ID);
		assertFalse(dao.exists(ID));

		doReturn(true).when(dao).existsWithId(ID);
		assertTrue(dao.exists(ID));
	}

	@Test
	public void shouldGetById() {
		ReadingGroup group = mock(ReadingGroup.class);
		doReturn(group).when(dao).getById(ID);
		assertSame(group, dao.get(ID));
	}

	@Test
	public void shouldGetAll() {
		List<ReadingGroup> all = mock(List.class);
		doReturn(all).when(dao).allOrderedByName();
		assertSame(all, dao.all());
	}

	@Test
	public void shouldSave() throws Exception {
		ReadingGroup group = mock(ReadingGroup.class);
		ReadingGroupRecord record = mock(ReadingGroupRecord.class);
		whenNew(ReadingGroupRecord.class).withArguments(group).thenReturn(record);
		doReturn(record).when(dao).save(record);
		when(record.id()).thenReturn(ID);

		assertEquals(ID, dao.save(group));
		verify(dao).save(record);
	}

	@Test
	public void shouldDelete() {
		doNothing().when(dao).deleteById(ID);
		dao.delete(ID);
		verify(dao).deleteById(ID);
	}
}