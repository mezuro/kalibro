package org.kalibro.core.persistence;

import static org.junit.Assert.assertFalse;
import static org.kalibro.core.model.ConfigurationFixtures.newConfiguration;
import static org.kalibro.core.model.MetricConfigurationFixtures.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.core.concurrent.VoidTask;
import org.kalibro.core.model.Configuration;
import org.kalibro.core.model.MetricConfiguration;
import org.kalibro.tests.UnitTest;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
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
		recordManager = PowerMockito.mock(RecordManager.class);
		configurationDao = PowerMockito.mock(ConfigurationDatabaseDao.class);
		PowerMockito.when(configurationDao.getConfiguration(configuration.getName())).thenReturn(configuration);
		PowerMockito.whenNew(ConfigurationDatabaseDao.class).withArguments(recordManager).
			thenReturn(configurationDao);
		dao = PowerMockito.spy(new MetricConfigurationDatabaseDao(recordManager));
	}

	@Test
	public void shouldAddMetricConfiguration() {
		dao.save(locConfiguration, configuration.getName());

		String locName = locConfiguration.getMetric().getName();
		assertDeepEquals(locConfiguration, configuration.getConfigurationFor(locName));
		Mockito.verify(configurationDao).save(configuration);
	}

	@Test
	public void shouldReplaceMetricConfiguration() {
		cboConfiguration = newMetricConfiguration("cbo");
		cboConfiguration.setWeight(42.0);
		dao.save(cboConfiguration, configuration.getName());

		String cboName = cboConfiguration.getMetric().getName();
		assertDeepEquals(cboConfiguration, configuration.getConfigurationFor(cboName));
		Mockito.verify(configurationDao).save(configuration);
	}

	@Test
	public void shouldGetMetricConfiguration() {
		String configurationName = configuration.getName();
		String metricName = cboConfiguration.getMetric().getName();
		assertDeepEquals(cboConfiguration, dao.getMetricConfiguration(configurationName, metricName));
	}

	@Test
	public void shouldRemoveMetricConfiguration() {
		String cboName = cboConfiguration.getMetric().getName();
		dao.removeMetricConfiguration(configuration.getName(), cboName);

		assertFalse(configuration.containsMetric(cboName));
		Mockito.verify(configurationDao).save(configuration);
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