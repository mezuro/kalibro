package org.kalibro.core.persistence;

import static org.junit.Assert.assertSame;
import static org.powermock.api.mockito.PowerMockito.*;

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
	public void shouldSaveAndSetId() throws Exception {
		ReadingGroup group = mock(ReadingGroup.class);
		ReadingGroupRecord record = mock(ReadingGroupRecord.class);
		whenNew(ReadingGroupRecord.class).withArguments(group).thenReturn(record);
		when(recordManager.save(record)).thenReturn(record);
		when(record.convert()).thenReturn(group);
		when(group.getId()).thenReturn(42L);

		dao.save(group);
		verify(group).setId(42L);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldDelete() {
		doNothing().when(dao).deleteById(42L);
		dao.delete(42L);
		verify(dao).deleteById(42L);
	}
}