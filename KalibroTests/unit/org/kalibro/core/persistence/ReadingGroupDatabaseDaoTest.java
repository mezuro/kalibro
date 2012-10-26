package org.kalibro.core.persistence;

import static org.junit.Assert.*;

import java.util.Random;

import javax.persistence.TypedQuery;

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
		when(record.convert()).thenReturn(group);
		dao = spy(new ReadingGroupDatabaseDao(null));
	}

	@Test
	public void shouldGetReadingGroupOfMetricConfiguration() {
		String from = "MetricConfiguration metricConfiguration JOIN metricConfiguration.readingGroup readingGroup";
		TypedQuery<ReadingGroupRecord> query = mock(TypedQuery.class);
		doReturn(query).when(dao).createRecordQuery(from, "metricConfiguration.id = :id");
		when(query.getSingleResult()).thenReturn(record);

		assertSame(group, dao.readingGroupOf(ID));
		verify(query).setParameter("id", ID);
	}

	@Test
	public void shouldSave() {
		doReturn(record).when(dao).save(record);
		assertEquals(ID, dao.save(group));
		verify(dao).save(record);
	}
}