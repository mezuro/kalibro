package org.kalibro.core.persistence;

import static org.junit.Assert.assertNotSame;
import static org.kalibro.ConfigurationFixtures.newConfiguration;
import static org.kalibro.MetricConfigurationFixtures.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.kalibro.Configuration;
import org.kalibro.MetricConfiguration;
import org.kalibro.SupportedDatabase;
import org.kalibro.core.concurrent.VoidTask;
import org.kalibro.dao.ConfigurationDao;
import org.kalibro.dao.DaoFactory;
import org.kalibro.dao.MetricConfigurationDao;
import org.kalibro.tests.AcceptanceTest;

@RunWith(Parameterized.class)
public class MetricConfigurationDatabaseTest extends AcceptanceTest {

	private Configuration configuration;

	private ConfigurationDao configurationDao;
	private MetricConfigurationDao dao;

	public MetricConfigurationDatabaseTest(SupportedDatabase databaseType) {
		super(databaseType);
	}

	@Before
	public void setUp() {
		configuration = newConfiguration("cbo");
		configurationDao = DaoFactory.getConfigurationDao();
		dao = DaoFactory.getMetricConfigurationDao();
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