package org.kalibro.core.persistence;

import static org.junit.Assert.assertEquals;

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

	private ReadingGroup group;
	private ReadingGroupRecord record;
	private ReadingGroupDatabaseDao dao;

	@Before
	public void setUp() throws Exception {
		group = mock(ReadingGroup.class);
		record = mock(ReadingGroupRecord.class);
		whenNew(ReadingGroupRecord.class).withArguments(group).thenReturn(record);
		when(record.id()).thenReturn(ID);
		dao = spy(new ReadingGroupDatabaseDao(null));
	}

	@Test
	public void shouldSave() {
		doReturn(record).when(dao).save(record);
		assertEquals(ID, dao.save(group));
		verify(dao).save(record);
	}
}