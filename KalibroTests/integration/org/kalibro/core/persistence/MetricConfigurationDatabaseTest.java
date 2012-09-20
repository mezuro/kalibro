package org.kalibro.core.persistence;

import static org.junit.Assert.assertNotSame;
import static org.kalibro.core.model.ConfigurationFixtures.newConfiguration;
import static org.kalibro.core.model.MetricConfigurationFixtures.*;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.Configuration;
import org.kalibro.core.concurrent.VoidTask;
import org.kalibro.core.model.MetricConfiguration;

public abstract class MetricConfigurationDatabaseTest extends DatabaseTestCase {

	private Configuration configuration;

	private ConfigurationDatabaseDao configurationDao;
	private MetricConfigurationDatabaseDao dao;

	@Before
	public void setUp() {
		configuration = newConfiguration("cbo");
		configurationDao = daoFactory.createConfigurationDao();
		dao = daoFactory.createMetricConfigurationDao();
		configurationDao.save(configuration);
	}

	@Test
	public void shouldRetrieveSavedMetricConfiguration() {
		MetricConfiguration locConfiguration = metricConfiguration("loc");
		String configurationName = configuration.getName();
		dao.save(locConfiguration, configurationName);

		String metricName = locConfiguration.getMetric().getName();
		MetricConfiguration retrieved = dao.getMetricConfiguration(configurationName, metricName);
		assertNotSame(locConfiguration, retrieved);
		assertDeepEquals(locConfiguration, retrieved);
	}

	@Test
	public void shouldReplaceMetricConfiguration() {
		MetricConfiguration cboConfiguration = newMetricConfiguration("cbo");
		String configurationName = configuration.getName();
		cboConfiguration.setWeight(42.0);
		dao.save(cboConfiguration, configurationName);

		String metricName = cboConfiguration.getMetric().getName();
		MetricConfiguration retrieved = dao.getMetricConfiguration(configurationName, metricName);
		assertDoubleEquals(42.0, retrieved.getWeight());
	}

	@Test
	public void shouldRemoveConfigurationByName() {
		final MetricConfiguration cboConfiguration = metricConfiguration("cbo");
		final String configurationName = configuration.getName();
		final String metricName = cboConfiguration.getMetric().getName();

		assertDeepEquals(cboConfiguration, dao.getMetricConfiguration(configurationName, metricName));

		dao.removeMetricConfiguration(configurationName, metricName);
		assertThat(new VoidTask() {

			@Override
			protected void perform() {
				dao.getMetricConfiguration(configurationName, metricName);
			}
		}).throwsException().withMessage("No configuration found for metric: " + metricName);
	}
}