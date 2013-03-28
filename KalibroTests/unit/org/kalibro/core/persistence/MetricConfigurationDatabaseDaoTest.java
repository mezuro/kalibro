package org.kalibro.core.persistence;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;
import org.kalibro.MetricConfiguration;
import org.kalibro.core.persistence.record.MetricConfigurationRecord;
import org.powermock.core.classloader.annotations.PrepareForTest;

@PrepareForTest(MetricConfigurationDatabaseDao.class)
public class MetricConfigurationDatabaseDaoTest extends
	DatabaseDaoTestCase<MetricConfiguration, MetricConfigurationRecord, MetricConfigurationDatabaseDao> {

	private static final Long ID = new Random().nextLong();
	private static final Long CONFIGURATION_ID = new Random().nextLong();

	@Test
	public void shouldGetMetricConfigurationsOfConfiguration() {
		assertDeepEquals(set(entity), dao.metricConfigurationsOf(CONFIGURATION_ID));

		verify(dao).createRecordQuery("metricConfiguration.configuration = :cId");
		verify(query).setParameter("cId", CONFIGURATION_ID);
	}

	@Test
	public void shouldSave() throws Exception {
		when(record.id()).thenReturn(ID);
		assertEquals(ID, dao.save(entity, CONFIGURATION_ID));

		verifyNew(MetricConfigurationRecord.class).withArguments(entity, CONFIGURATION_ID);
		verify(dao).save(record);
	}
}