package org.kalibro.core.persistence;

import static org.junit.Assert.assertEquals;

import java.util.Random;

import org.junit.Test;
import org.kalibro.ReadingGroup;
import org.kalibro.core.persistence.record.ReadingGroupRecord;
import org.powermock.core.classloader.annotations.PrepareForTest;

@PrepareForTest(ReadingGroupDatabaseDao.class)
public class ReadingGroupDatabaseDaoTest extends
	DatabaseDaoTestCase<ReadingGroup, ReadingGroupRecord, ReadingGroupDatabaseDao> {

	private static final Long ID = Math.abs(new Random().nextLong());

	@Test
	public void shouldSave() throws Exception {
		when(record.id()).thenReturn(ID);
		assertEquals(ID, dao.save(entity));

		verifyNew(ReadingGroupRecord.class).withArguments(entity);
		verify(dao).save(record);
	}
}