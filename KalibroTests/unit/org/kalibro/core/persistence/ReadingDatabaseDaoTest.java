package org.kalibro.core.persistence;

import static org.junit.Assert.assertEquals;

import java.util.Random;

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

	private static final Long READING_ID = new Random().nextLong();
	private static final Long GROUP_ID = new Random().nextLong();

	private Reading reading;
	private ReadingRecord record;

	private ReadingDatabaseDao dao;

	@Before
	public void setUp() throws Exception {
		reading = mock(Reading.class);
		record = mock(ReadingRecord.class);
		when(reading.getGroupId()).thenReturn(GROUP_ID);
		whenNew(ReadingRecord.class).withArguments(reading, GROUP_ID).thenReturn(record);
		when(record.convert()).thenReturn(reading);
		when(record.id()).thenReturn(READING_ID);
		dao = spy(new ReadingDatabaseDao(null));
	}

	@Test
	public void shouldGetReadingsOfGroup() {
		TypedQuery<ReadingRecord> query = mock(TypedQuery.class);
		doReturn(query).when(dao).createRecordQuery("WHERE reading.group.id = :groupId");
		when(query.getResultList()).thenReturn(asList(record));

		assertDeepEquals(asSet(reading), dao.readingsOf(GROUP_ID));
		verify(query).setParameter("groupId", GROUP_ID);
	}

	@Test
	public void shouldSave() {
		doReturn(record).when(dao).save(record);
		assertEquals(READING_ID, dao.save(reading));
		verify(dao).save(record);
	}
}