package org.kalibro;

import static org.junit.Assert.*;
import static org.kalibro.ConfigurationFixtures.newConfiguration;
import static org.kalibro.MetricConfigurationFixtures.metricConfiguration;
import static org.kalibro.MetricFixtures.*;

import java.io.File;
import java.util.SortedSet;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.core.abstractentity.AbstractEntity;
import org.kalibro.core.concurrent.VoidTask;
import org.kalibro.dao.ConfigurationDao;
import org.kalibro.dao.DaoFactory;
import org.kalibro.dao.MetricConfigurationDao;
import org.kalibro.tests.UnitTest;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({AbstractEntity.class, DaoFactory.class})
public class ConfigurationTest extends UnitTest {

	private NativeMetric cbo, lcom4;
	private CompoundMetric sc;
	private ConfigurationDao dao;

	private Configuration configuration;

	@Before
	public void setUp() {
		sc = newSc();
		cbo = analizoMetric("cbo");
		lcom4 = analizoMetric("lcom4");
		configuration = newConfiguration("cbo", "lcom4");
		mockDao();
	}

	private void mockDao() {
		dao = mock(ConfigurationDao.class);
		mockStatic(DaoFactory.class);
		when(DaoFactory.getConfigurationDao()).thenReturn(dao);
	}

	@Test
	public void shouldSortByName() {
		assertSorted(new Configuration("A"), new Configuration("B"), new Configuration("X"), new Configuration("Z"));
	}

	@Test
	public void shouldImportFromFile() throws Exception {
		File file = mock(File.class);
		mockStatic(AbstractEntity.class);
		when(AbstractEntity.class, "importFrom", file, Configuration.class).thenReturn(configuration);

		assertSame(configuration, Configuration.importFrom(file));
		verifyPrivate(AbstractEntity.class).invoke("importFrom", file, Configuration.class);
	}

	@Test
	public void shouldGetAllConfigurations() {
		SortedSet<Configuration> configurations = mock(SortedSet.class);
		when(dao.all()).thenReturn(configurations);
		assertSame(configurations, Configuration.all());
	}

	@Test
	public void checkDefaultConfiguration() {
		configuration = new Configuration();
		assertFalse(configuration.hasId());
		assertEquals("", configuration.getName());
		assertEquals("", configuration.getDescription());
		assertTrue(configuration.getMetricConfigurations().isEmpty());
	}

	@Test
	public void shouldSetConfigurationOnMetricConfigurations() {
		MetricConfiguration metricConfiguration = mock(MetricConfiguration.class);
		configuration.setMetricConfigurations(asSortedSet(metricConfiguration));
		assertDeepEquals(asSet(metricConfiguration), configuration.getMetricConfigurations());
		verify(metricConfiguration).setConfiguration(configuration);
	}

	@Test
	public void shouldSetMetricConfigurationsWithoutTouchingThem() {
		// required for lazy loading
		SortedSet<MetricConfiguration> metricConfigurations = mock(SortedSet.class);
		configuration.setMetricConfigurations(metricConfigurations);
		verifyZeroInteractions(metricConfigurations);
	}

	@Test
	public void toStringShouldBeConfigurationName() {
		assertEquals(configuration.getName(), "" + configuration);
	}

	@Test
	public void shouldAddMetricConfigurationIfItDoesNotConflictWithExistingOnes() {
		MetricConfiguration metricConfiguration = mock(MetricConfiguration.class);
		SortedSet<MetricConfiguration> existents = configuration.getMetricConfigurations();
		configuration.addMetricConfiguration(metricConfiguration);

		InOrder order = Mockito.inOrder(metricConfiguration);
		for (MetricConfiguration existent : existents)
			order.verify(metricConfiguration).assertNoConflictWith(existent);
		order.verify(metricConfiguration).setConfiguration(configuration);

		assertTrue(configuration.getMetricConfigurations().contains(metricConfiguration));
	}

	@Test
	public void shouldRemoveMetricConfiguration() {
		MetricConfiguration metricConfiguration = mock(MetricConfiguration.class);
		SortedSet<MetricConfiguration> metricConfigurations = spy(asSortedSet(metricConfiguration));
		configuration.setMetricConfigurations(metricConfigurations);

		configuration.removeMetricConfiguration(metricConfiguration);
		verify(metricConfigurations).remove(metricConfiguration);
		verify(metricConfiguration).setConfiguration(null);
	}

	@Test
	public void shouldGetCompoundMetrics() {
		assertTrue(configuration.getCompoundMetrics().isEmpty());
		configuration.addMetricConfiguration(new MetricConfiguration(sc));
		assertDeepEquals(asSet(sc), configuration.getCompoundMetrics());
	}

	@Test
	public void shouldRetrieveNativeMetricsPerBaseTool() {
		assertDeepEquals(asMap("Analizo", asSet(cbo, lcom4)), configuration.getNativeMetrics());
	}

	@Test
	public void shouldAnswerIfContainsMetric() {
		assertTrue(configuration.containsMetric(cbo));
		assertTrue(configuration.containsMetric(lcom4));
		assertFalse(configuration.containsMetric(sc));
	}

	@Test
	public void shouldGetConfigurationForMetric() {
		assertDeepEquals(metricConfiguration("cbo"), configuration.getConfigurationFor(cbo));
		assertDeepEquals(metricConfiguration("lcom4"), configuration.getConfigurationFor(lcom4));
		assertThat(getScConfiguration()).throwsException()
			.withMessage("No configuration found for metric: Structural complexity");
	}

	private VoidTask getScConfiguration() {
		return new VoidTask() {

			@Override
			protected void perform() {
				configuration.getConfigurationFor(sc);
			}
		};
	}

	@Test
	public void shouldValidateScripts() {
		MetricConfiguration scConfiguration = new MetricConfiguration(sc);
		configuration.addMetricConfiguration(scConfiguration);
		configuration.validateScripts();

		sc.setScript("return null;");
		assertThat(validateScripts()).throwsException()
			.withMessage("Error evaluating Javascript for: structuralComplexity");
	}

	private VoidTask validateScripts() {
		return new VoidTask() {

			@Override
			protected void perform() throws Exception {
				configuration.validateScripts();
			}
		};
	}

	@Test
	public void shouldUpdateIdAndMetricConfigurationsOnSave() {
		MetricConfiguration metricConfiguration = mock(MetricConfiguration.class);
		MetricConfigurationDao metricConfigurationDao = mock(MetricConfigurationDao.class);
		when(dao.save(configuration)).thenReturn(42L);
		when(DaoFactory.getMetricConfigurationDao()).thenReturn(metricConfigurationDao);
		when(metricConfigurationDao.metricConfigurationsOf(42L)).thenReturn(asSortedSet(metricConfiguration));

		assertFalse(configuration.hasId());
		configuration.save();
		assertEquals(42L, configuration.getId().longValue());
		assertDeepEquals(asSet(metricConfiguration), configuration.getMetricConfigurations());
	}

	@Test
	public void shouldRequiredNameToSave() {
		configuration.setName(" ");
		assertThat(save()).throwsException().withMessage("Configuration requires name.");
	}

	private VoidTask save() {
		return new VoidTask() {

			@Override
			protected void perform() {
				configuration.save();
			}
		};
	}

	@Test
	public void shouldDeleteIfHasId() {
		assertFalse(configuration.hasId());
		configuration.delete();
		verify(dao, never()).delete(any(Long.class));

		configuration.setId(42L);

		assertTrue(configuration.hasId());
		configuration.delete();
		verify(dao).delete(42L);
		assertFalse(configuration.hasId());
	}

	@Test
	public void shouldNotifyMetricConfigurationsOfDeletion() {
		MetricConfiguration metricConfiguration = mock(MetricConfiguration.class);
		configuration.setMetricConfigurations(asSortedSet(metricConfiguration));
		configuration.setId(42L);

		configuration.delete();
		verify(metricConfiguration).deleted();
	}

	@Test
	public void toStringShouldBeName() {
		assertEquals(configuration.getName(), "" + configuration);
	}
}