package org.kalibro.core.persistence.database;

import static org.junit.Assert.*;
import static org.kalibro.core.model.ConfigurationFixtures.*;
import static org.kalibro.core.model.MetricConfigurationFixtures.*;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.core.concurrent.Task;
import org.kalibro.core.model.Configuration;
import org.kalibro.core.model.MetricConfiguration;

public abstract class MetricConfigurationDatabaseTest extends DatabaseTestCase {

	private Configuration configuration;

	private ConfigurationDatabaseDao configurationDao;
	private MetricConfigurationDatabaseDao dao;

	@Before
	public void setUp() {
		configuration = newConfiguration("cbo");
		configurationDao = daoFactory.getConfigurationDao();
		dao = daoFactory.getMetricConfigurationDao();
		configurationDao.save(configuration);
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void shouldRetrieveSavedMetricConfiguration() {
		MetricConfiguration locConfiguration = metricConfiguration("loc");
		String configurationName = configuration.getName();
		dao.save(locConfiguration, configurationName);

		String metricName = locConfiguration.getMetric().getName();
		MetricConfiguration retrieved = dao.getMetricConfiguration(configurationName, metricName);
		assertNotSame(locConfiguration, retrieved);
		assertDeepEquals(locConfiguration, retrieved);
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void shouldReplaceMetricConfiguration() {
		MetricConfiguration cboConfiguration = newMetricConfiguration("cbo");
		String configurationName = configuration.getName();
		cboConfiguration.setWeight(42.0);
		dao.save(cboConfiguration, configurationName);

		String metricName = cboConfiguration.getMetric().getName();
		MetricConfiguration retrieved = dao.getMetricConfiguration(configurationName, metricName);
		assertDoubleEquals(42.0, retrieved.getWeight());
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void shouldRemoveConfigurationByName() {
		final MetricConfiguration cboConfiguration = metricConfiguration("cbo");
		final String configurationName = configuration.getName();
		final String metricName = cboConfiguration.getMetric().getName();

		assertDeepEquals(cboConfiguration, dao.getMetricConfiguration(configurationName, metricName));

		dao.removeMetricConfiguration(configurationName, metricName);
		checkKalibroException(new Task() {

			@Override
			public void perform() {
				dao.getMetricConfiguration(configurationName, metricName);
			}
		}, "No configuration found for metric: " + metricName);
	}
}