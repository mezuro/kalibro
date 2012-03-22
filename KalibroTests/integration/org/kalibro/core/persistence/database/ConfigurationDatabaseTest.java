package org.kalibro.core.persistence.database;

import static org.junit.Assert.*;
import static org.kalibro.core.model.ConfigurationFixtures.*;
import static org.kalibro.core.model.NativeMetricFixtures.*;

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
		kalibroConfiguration = kalibroConfiguration();
		simpleConfiguration = kalibroConfiguration();
		simpleConfiguration.setName("Simple");
		simpleConfiguration.addMetricConfiguration(new MetricConfiguration(CompoundMetricFixtures.sc()));
		dao = daoFactory.getConfigurationDao();
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void shouldListSavedConfigurationNames() {
		assertDeepEquals(dao.getConfigurationNames(), kalibroConfiguration.getName());

		dao.save(simpleConfiguration);
		assertDeepEquals(dao.getConfigurationNames(), simpleConfiguration.getName(), kalibroConfiguration.getName());
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
		assertDeepEquals(kalibroConfiguration, dao.getConfigurationFor(project.getName()));
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void shouldRemoveConfigurationByName() {
		dao.save(simpleConfiguration);
		assertDeepEquals(dao.getConfigurationNames(), simpleConfiguration.getName(), kalibroConfiguration.getName());

		dao.removeConfiguration(kalibroConfiguration.getName());
		assertDeepEquals(dao.getConfigurationNames(), simpleConfiguration.getName());

		dao.removeConfiguration(simpleConfiguration.getName());
		assertTrue(dao.getConfigurationNames().isEmpty());
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void shouldNotRetrieveUnsavedConfiguration() {
		checkException(new Task() {

			@Override
			public void perform() {
				dao.getConfiguration(simpleConfiguration.getName());
			}
		}, NoResultException.class, "There is no configuration named '" + simpleConfiguration.getName() + "'");
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void shouldSaveAndRetrieveMetric() {
		NativeMetric loc = nativeMetric("loc");
		assertTrue(kalibroConfiguration.contains(loc));
		assertTrue(retrieve(kalibroConfiguration).contains(loc));

		kalibroConfiguration.getConfigurationFor(loc).setWeight(42.0);
		dao.save(kalibroConfiguration);
		assertDoubleEquals(42.0, retrieve(kalibroConfiguration).getConfigurationFor(loc).getWeight());
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void configurationsShouldNotShareMetrics() {
		NativeMetric cbo = nativeMetric("cbo");
		assertDeepEquals(kalibroConfiguration.getConfigurationFor(cbo), simpleConfiguration.getConfigurationFor(cbo));
		dao.save(simpleConfiguration);

		simpleConfiguration.getConfigurationFor(cbo).setAggregationForm(Statistic.STANDARD_DEVIATION);
		dao.save(simpleConfiguration);

		assertEquals(Statistic.AVERAGE, retrieve(kalibroConfiguration).getConfigurationFor(cbo).getAggregationForm());
	}

	private Configuration retrieve(Configuration configuration) {
		return dao.getConfiguration(configuration.getName());
	}
}