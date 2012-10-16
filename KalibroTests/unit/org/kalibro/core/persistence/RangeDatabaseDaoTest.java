package org.kalibro.core.persistence;

import static org.junit.Assert.assertEquals;

import java.util.Random;

import javax.persistence.TypedQuery;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.Range;
import org.kalibro.core.persistence.record.RangeRecord;
import org.kalibro.tests.UnitTest;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(RangeDatabaseDao.class)
public class RangeDatabaseDaoTest extends UnitTest {

	private static final Long ID = new Random().nextLong();
	private static final Long CONFIGURATION_ID = new Random().nextLong();

	private Range range;
	private RangeRecord record;

	private RangeDatabaseDao dao;

	@Before
	public void setUp() throws Exception {
		range = mock(Range.class);
		record = mock(RangeRecord.class);
		whenNew(RangeRecord.class).withArguments(range, CONFIGURATION_ID).thenReturn(record);
		when(record.convert()).thenReturn(range);
		when(record.id()).thenReturn(ID);
		dao = spy(new RangeDatabaseDao(null));
	}

	@Test
	public void shouldGetRangesOfMetricConfiguration() {
		TypedQuery<RangeRecord> query = mock(TypedQuery.class);
		doReturn(query).when(dao).createRecordQuery("WHERE range.configuration.id = :configurationId");
		when(query.getResultList()).thenReturn(asList(record));

		assertDeepEquals(asSet(range), dao.rangesOf(CONFIGURATION_ID));
		verify(query).setParameter("configurationId", CONFIGURATION_ID);
	}

	@Test
	public void shouldSave() {
		doReturn(record).when(dao).save(record);
		assertEquals(ID, dao.save(range, CONFIGURATION_ID));
		verify(dao).save(record);
	}
}