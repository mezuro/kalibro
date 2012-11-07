package org.kalibro.core.persistence;

import static org.junit.Assert.assertEquals;

import java.util.Random;

import org.junit.Test;
import org.kalibro.Range;
import org.kalibro.core.persistence.record.RangeRecord;
import org.powermock.core.classloader.annotations.PrepareForTest;

@PrepareForTest(RangeDatabaseDao.class)
public class RangeDatabaseDaoTest extends DatabaseDaoTestCase<Range, RangeRecord, RangeDatabaseDao> {

	private static final Long ID = new Random().nextLong();
	private static final Long CONFIGURATION_ID = new Random().nextLong();

	@Test
	public void shouldGetRangesOfMetricConfiguration() {
		assertDeepEquals(set(entity), dao.rangesOf(CONFIGURATION_ID));
		verify(dao).createRecordQuery("range.configuration.id = :configurationId");
		verify(query).setParameter("configurationId", CONFIGURATION_ID);
	}

	@Test
	public void shouldSave() throws Exception {
		when(record.id()).thenReturn(ID);
		assertEquals(ID, dao.save(entity, CONFIGURATION_ID));

		verifyNew(RangeRecord.class).withArguments(entity, CONFIGURATION_ID);
		verify(dao).save(record);
	}
}