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

	private Configuration kalibroForJava, configuration2;

	@Before
	public void setUp() {
		kalibroForJava = kalibroConfiguration();
		configuration2 = kalibroConfiguration();
		configuration2.setName("Configuration 2");
		configuration2.addMetricConfiguration(new MetricConfiguration(CompoundMetricFixtures.sc()));
		dao = daoFactory.getConfigurationDao();
		daoFactory.getBaseToolDao().save(BaseToolFixtures.analizoStub());
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void shouldListSavedConfigurationNames() {
		assertDeepEquals(dao.getConfigurationNames(), kalibroForJava.getName());

		dao.save(configuration2);
		assertDeepEquals(dao.getConfigurationNames(), configuration2.getName(), kalibroForJava.getName());
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void shouldRetrieveSavedConfiguration() {
		dao.save(configuration2);
		Configuration retrieved = retrieve(configuration2);
		assertNotSame(configuration2, retrieved);
		assertDeepEquals(configuration2, retrieved);
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void shouldRetrieveConfigurationForProject() {
		Project project = ProjectFixtures.helloWorld();
		daoFactory.getProjectDao().save(project);
		assertDeepEquals(kalibroForJava, dao.getConfigurationFor(project.getName()));
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void shouldRemoveConfigurationByName() {
		dao.save(configuration2);
		assertDeepEquals(dao.getConfigurationNames(), configuration2.getName(), kalibroForJava.getName());

		dao.removeConfiguration(kalibroForJava.getName());
		assertDeepEquals(dao.getConfigurationNames(), configuration2.getName());

		dao.removeConfiguration(configuration2.getName());
		assertTrue(dao.getConfigurationNames().isEmpty());
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void shouldNotRetrieveUnsavedConfiguration() {
		checkException(new Task() {

			@Override
			public void perform() {
				dao.getConfiguration(configuration2.getName());
			}
		}, NoResultException.class, "There is no configuration named '" + configuration2.getName() + "'");
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void shouldSaveAndRetrieveMetric() {
		NativeMetric loc = nativeMetric("loc");
		assertTrue(kalibroForJava.contains(loc));
		assertTrue(retrieve(kalibroForJava).contains(loc));

		kalibroForJava.getConfigurationFor(loc).setWeight(42.0);
		dao.save(kalibroForJava);
		assertDoubleEquals(42.0, retrieve(kalibroForJava).getConfigurationFor(loc).getWeight());
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void configurationsShouldNotShareMetrics() {
		NativeMetric nom = nativeMetric("nom");
		assertDeepEquals(kalibroForJava.getConfigurationFor(nom), configuration2.getConfigurationFor(nom));
		dao.save(configuration2);

		configuration2.getConfigurationFor(nom).setAggregationForm(Statistic.STANDARD_DEVIATION);
		dao.save(configuration2);

		assertEquals(Statistic.AVERAGE, retrieve(kalibroForJava).getConfigurationFor(nom).getAggregationForm());
	}

	private Configuration retrieve(Configuration configuration) {
		return dao.getConfiguration(configuration.getName());
	}
}