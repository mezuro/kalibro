package org.kalibro.core.persistence;

import static org.junit.Assert.assertEquals;

import java.util.Random;

import javax.persistence.TypedQuery;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.MetricConfiguration;
import org.kalibro.core.persistence.record.MetricConfigurationRecord;
import org.kalibro.tests.UnitTest;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(MetricConfigurationDatabaseDao.class)
public class MetricConfigurationDatabaseDaoTest extends UnitTest {

	private static final Long METRIC_CONFIGURATION_ID = new Random().nextLong();
	private static final Long CONFIGURATION_ID = new Random().nextLong();

	private MetricConfiguration metricConfiguration;
	private MetricConfigurationRecord record;

	private MetricConfigurationDatabaseDao dao;

	@Before
	public void setUp() throws Exception {
		metricConfiguration = mock(MetricConfiguration.class);
		record = mock(MetricConfigurationRecord.class);
		when(metricConfiguration.getConfigurationId()).thenReturn(CONFIGURATION_ID);
		whenNew(MetricConfigurationRecord.class).withArguments(metricConfiguration, CONFIGURATION_ID)
			.thenReturn(record);
		when(record.convert()).thenReturn(metricConfiguration);
		when(record.id()).thenReturn(METRIC_CONFIGURATION_ID);
		dao = spy(new MetricConfigurationDatabaseDao(null));
	}

	@Test
	public void shouldGetMetricConfigurationsOfConfiguration() {
		TypedQuery<MetricConfigurationRecord> query = mock(TypedQuery.class);
		doReturn(query).when(dao).createRecordQuery("WHERE metricConfiguration.configuration.id = :configurationId");
		when(query.getResultList()).thenReturn(asList(record));

		assertDeepEquals(asSet(metricConfiguration), dao.metricConfigurationsOf(CONFIGURATION_ID));
		verify(query).setParameter("configurationId", CONFIGURATION_ID);
	}

	@Test
	public void shouldSave() {
		doReturn(record).when(dao).save(record);
		assertEquals(METRIC_CONFIGURATION_ID, dao.save(metricConfiguration));
		verify(dao).save(record);
	}
}