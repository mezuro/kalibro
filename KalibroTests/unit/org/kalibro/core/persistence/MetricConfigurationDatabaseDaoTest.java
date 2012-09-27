package org.kalibro.core.persistence;

import static org.kalibro.ConfigurationFixtures.newConfiguration;
import static org.kalibro.MetricConfigurationFixtures.metricConfiguration;

import javax.persistence.TypedQuery;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.Configuration;
import org.kalibro.MetricConfiguration;
import org.kalibro.core.concurrent.VoidTask;
import org.kalibro.core.persistence.record.ConfigurationRecord;
import org.kalibro.tests.UnitTest;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(MetricConfigurationDatabaseDao.class)
public class MetricConfigurationDatabaseDaoTest extends UnitTest {

	private Configuration configuration;
	private MetricConfiguration cboConfiguration, locConfiguration;

	private RecordManager recordManager;
	private ConfigurationDatabaseDao configurationDao;

	private MetricConfigurationDatabaseDao dao;

	@Before
	public void setUp() throws Exception {
		configuration = newConfiguration("cbo");
		cboConfiguration = metricConfiguration("cbo");
		locConfiguration = metricConfiguration("loc");
		recordManager = mock(RecordManager.class);
		configurationDao = mock(ConfigurationDatabaseDao.class);

		ConfigurationRecord record = mock(ConfigurationRecord.class);
		TypedQuery<ConfigurationRecord> query = mock(TypedQuery.class);
		when(configurationDao.createRecordQuery("WHERE configuration.name = :name")).thenReturn(query);
		when(query.getSingleResult()).thenReturn(record);
		when(record.convert()).thenReturn(configuration);
		whenNew(ConfigurationDatabaseDao.class).withArguments(recordManager).thenReturn(configurationDao);
		dao = spy(new MetricConfigurationDatabaseDao(recordManager));
	}

	@Test
	public void shouldGetMetricConfiguration() {
		String configurationName = configuration.getName();
		String metricName = cboConfiguration.getMetric().getName();
		assertDeepEquals(cboConfiguration, dao.getMetricConfiguration(configurationName, metricName));
	}

	@Test
	public void shouldThrowExceptionForMetricConfigurationNotFound() {
		assertThat(new VoidTask() {

			@Override
			protected void perform() {
				String metricName = locConfiguration.getMetric().getName();
				dao.getMetricConfiguration(configuration.getName(), metricName);
			}
		}).throwsException().withMessage("No configuration found for metric: Lines of Code");
	}
}