package org.kalibro.core.persistence;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;
import org.kalibro.Reading;
import org.kalibro.core.persistence.record.ReadingRecord;
import org.powermock.core.classloader.annotations.PrepareForTest;

@PrepareForTest(ReadingDatabaseDao.class)
public class ReadingDatabaseDaoTest extends DatabaseDaoTestCase<Reading, ReadingRecord, ReadingDatabaseDao> {

	private static final Long ID = new Random().nextLong();
	private static final Long GROUP_ID = new Random().nextLong();

	@Test
	public void shouldGetReadingOfRange() {
		assertSame(entity, dao.readingOf(ID));

		String from = "Range range JOIN range.reading reading";
		verify(dao).createRecordQuery(from, "range.id = :rangeId");
		verify(query).setParameter("rangeId", ID);
	}

	@Test
	public void shouldGetReadingsOfGroup() {
		assertDeepEquals(set(entity), dao.readingsOf(GROUP_ID));

		verify(dao).createRecordQuery("reading.group.id = :groupId");
		verify(query).setParameter("groupId", GROUP_ID);
	}

	@Test
	public void shouldSave() throws Exception {
		when(record.id()).thenReturn(ID);
		assertEquals(ID, dao.save(entity, GROUP_ID));

		verifyNew(ReadingRecord.class).withArguments(entity, GROUP_ID);
		verify(dao).save(record);
	}
}