package org.kalibro.core.persistence.database;

import static org.junit.Assert.*;
import static org.kalibro.core.model.ConfigurationFixtures.*;
import static org.kalibro.core.model.MetricFixtures.*;

import javax.persistence.NoResultException;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.core.concurrent.Task;
import org.kalibro.core.model.*;
import org.kalibro.core.model.enums.Statistic;

public abstract class ConfigurationDatabaseTest extends DatabaseTestCase {

	private ConfigurationDatabaseDao dao;

	private Configuration kalibroConfiguration, simpleConfiguration;

	@Before
	public void setUp() {
		kalibroConfiguration = newKalibroConfiguration();
		simpleConfiguration = newConfiguration("cbo", "lcom4");
		simpleConfiguration.setName("Simple");
		simpleConfiguration.addMetricConfiguration(new MetricConfiguration(sc()));
		dao = daoFactory.getConfigurationDao();
		dao.save(kalibroConfiguration);
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void shouldListSavedConfigurationNames() {
		assertDeepEquals(dao.getConfigurationNames(), kalibroConfiguration.getName());

		dao.save(simpleConfiguration);
		assertDeepEquals(dao.getConfigurationNames(), kalibroConfiguration.getName(), simpleConfiguration.getName());
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void shouldRetrieveSavedConfiguration() {
		dao.save(simpleConfiguration);
		Configuration retrieved = retrieve(simpleConfiguration);
		assertNotSame(simpleConfiguration, retrieved);
		assertDeepEquals(simpleConfiguration, retrieved);
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void shouldRetrieveConfigurationForProject() {
		Project project = ProjectFixtures.helloWorld();
		daoFactory.getProjectDao().save(project);
		Configuration retrieved = dao.getConfigurationFor(project.getName());
		kalibroConfiguration.setId(retrieved.getId());
		assertDeepEquals(kalibroConfiguration, retrieved);
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void shouldRemoveConfigurationByName() {
		dao.save(simpleConfiguration);
		assertDeepEquals(dao.getConfigurationNames(), kalibroConfiguration.getName(), simpleConfiguration.getName());

		dao.removeConfiguration(kalibroConfiguration.getName());
		assertDeepEquals(dao.getConfigurationNames(), simpleConfiguration.getName());

		dao.removeConfiguration(simpleConfiguration.getName());
		assertTrue(dao.getConfigurationNames().isEmpty());
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void shouldNotRetrieveUnsavedConfiguration() {
		checkKalibroException(new Task() {

			@Override
			public void perform() {
				dao.getConfiguration(simpleConfiguration.getName());
			}
		}, "There is no configuration named '" + simpleConfiguration.getName() + "'", NoResultException.class);
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void shouldSaveAndRetrieveMetric() {
		NativeMetric loc = analizoMetric("loc");
		String locName = loc.getName();
		assertTrue(kalibroConfiguration.containsMetric(locName));
		kalibroConfiguration = retrieve(kalibroConfiguration);
		assertTrue(kalibroConfiguration.containsMetric(locName));

		kalibroConfiguration.getConfigurationFor(locName).setWeight(42.0);
		dao.save(kalibroConfiguration);
		assertDoubleEquals(42.0, retrieve(kalibroConfiguration).getConfigurationFor(locName).getWeight());
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void configurationsShouldNotShareMetrics() {
		NativeMetric cbo = analizoMetric("cbo");
		String cboName = cbo.getName();
		assertDeepEquals(kalibroConfiguration.getConfigurationFor(cboName),
			simpleConfiguration.getConfigurationFor(cboName));
		dao.save(simpleConfiguration);

		simpleConfiguration.getConfigurationFor(cboName).setAggregationForm(Statistic.STANDARD_DEVIATION);
		dao.save(simpleConfiguration);

		assertEquals(Statistic.AVERAGE,
			retrieve(kalibroConfiguration).getConfigurationFor(cboName).getAggregationForm());
	}

	private Configuration retrieve(Configuration configuration) {
		return dao.getConfiguration(configuration.getName());
	}
}