package org.kalibro.core.persistence;

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
		dao = daoFactory.createConfigurationDao();
		dao.save(kalibroConfiguration);
	}

	@Test
	public void shouldListSavedConfigurationNames() {
		assertDeepList(dao.getConfigurationNames(), kalibroConfiguration.getName());

		dao.save(simpleConfiguration);
		assertDeepList(dao.getConfigurationNames(), kalibroConfiguration.getName(), simpleConfiguration.getName());
	}

	@Test
	public void shouldRetrieveSavedConfiguration() {
		dao.save(simpleConfiguration);
		Configuration retrieved = retrieve(simpleConfiguration);
		assertNotSame(simpleConfiguration, retrieved);
		assertDeepEquals(simpleConfiguration, retrieved);
	}

	@Test
	public void shouldRetrieveConfigurationForProject() {
		Project project = ProjectFixtures.helloWorld();
		daoFactory.createProjectDao().save(project);
		Configuration retrieved = dao.getConfigurationFor(project.getName());
		kalibroConfiguration.setId(retrieved.getId());
		assertDeepEquals(kalibroConfiguration, retrieved);
	}

	@Test
	public void shouldRemoveConfigurationByName() {
		dao.save(simpleConfiguration);
		assertDeepList(dao.getConfigurationNames(), kalibroConfiguration.getName(), simpleConfiguration.getName());

		dao.removeConfiguration(kalibroConfiguration.getName());
		assertDeepList(dao.getConfigurationNames(), simpleConfiguration.getName());

		dao.removeConfiguration(simpleConfiguration.getName());
		assertTrue(dao.getConfigurationNames().isEmpty());
	}

	@Test
	public void shouldNotRetrieveUnsavedConfiguration() {
		assertThat(new Task() {

			@Override
			public void perform() {
				dao.getConfiguration(simpleConfiguration.getName());
			}
		}).throwsException().withMessage("There is no configuration named '" + simpleConfiguration.getName() + "'")
			.withCause(NoResultException.class);
	}

	@Test
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

	@Test
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