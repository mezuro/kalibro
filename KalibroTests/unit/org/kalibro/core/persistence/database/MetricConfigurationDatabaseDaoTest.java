package org.kalibro.core.persistence.database;

import javax.persistence.EntityNotFoundException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.concurrent.Task;
import org.kalibro.core.model.Configuration;
import org.kalibro.core.model.ConfigurationFixtures;
import org.kalibro.core.model.MetricConfiguration;
import org.kalibro.core.model.MetricConfigurationFixtures;
import org.kalibro.core.persistence.database.entities.MetricConfigurationRecord;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(MetricConfigurationDatabaseDao.class)
public class MetricConfigurationDatabaseDaoTest extends KalibroTestCase {

	private Configuration configuration;
	private MetricConfiguration cboConfiguration, ditConfiguration;

	private DatabaseManager databaseManager;
	private ConfigurationDatabaseDao configurationDao;

	private MetricConfigurationDatabaseDao dao;

	@Before
	public void setUp() throws Exception {
		configuration = ConfigurationFixtures.simpleConfiguration();
		cboConfiguration = MetricConfigurationFixtures.configuration("cbo");
		ditConfiguration = MetricConfigurationFixtures.configuration("dit");
		databaseManager = PowerMockito.mock(DatabaseManager.class);
		configurationDao = PowerMockito.mock(ConfigurationDatabaseDao.class);
		PowerMockito.when(configurationDao.getConfiguration(configuration.getName())).thenReturn(configuration);
		PowerMockito.whenNew(ConfigurationDatabaseDao.class).withArguments(databaseManager).
			thenReturn(configurationDao);
		dao = PowerMockito.spy(new MetricConfigurationDatabaseDao(databaseManager));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSave() {
		dao.save(ditConfiguration, configuration.getName());

		ArgumentCaptor<MetricConfigurationRecord> captor = ArgumentCaptor.forClass(MetricConfigurationRecord.class);
		Mockito.verify(databaseManager).save(captor.capture());
		assertDeepEquals(ditConfiguration, captor.getValue().convert());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldGetMetricConfiguration() {
		String configurationName = configuration.getName();
		String metricName = cboConfiguration.getMetric().getName();
		assertDeepEquals(cboConfiguration, dao.getMetricConfiguration(configurationName, metricName));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRemoveMetricConfiguration() {
		String metricName = cboConfiguration.getMetric().getName();
		dao.removeMetricConfiguration(configuration.getName(), metricName);

		ArgumentCaptor<MetricConfigurationRecord> captor = ArgumentCaptor.forClass(MetricConfigurationRecord.class);
		Mockito.verify(databaseManager).delete(captor.capture());
		assertDeepEquals(cboConfiguration, captor.getValue().convert());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldThrowExceptionForMetricConfigurationNotFound() {
		String expectedMessage = "Metric 'Depth of Inheritance Tree' not found in configuration 'Kalibro for Java'";
		checkException(new Task() {

			@Override
			public void perform() throws Exception {
				String metricName = ditConfiguration.getMetric().getName();
				dao.getMetricConfiguration(configuration.getName(), metricName);
			}
		}, EntityNotFoundException.class, expectedMessage);
	}
}