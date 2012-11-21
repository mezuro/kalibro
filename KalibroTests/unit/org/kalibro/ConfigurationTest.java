package org.kalibro;

import static org.junit.Assert.*;

import java.io.File;
import java.util.SortedSet;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.core.abstractentity.AbstractEntity;
import org.kalibro.core.concurrent.TaskMatcher;
import org.kalibro.core.concurrent.VoidTask;
import org.kalibro.core.processing.ScriptValidator;
import org.kalibro.dao.ConfigurationDao;
import org.kalibro.dao.DaoFactory;
import org.kalibro.tests.UnitTest;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

@RunWith(PowerMockRunner.class)
@PrepareForTest({AbstractEntity.class, DaoFactory.class, FileUtils.class, ScriptValidator.class})
public class ConfigurationTest extends UnitTest {

	private ConfigurationDao dao;

	private CompoundMetric sc;
	private NativeMetric cbo, lcom4;
	private Configuration configuration;

	@Before
	public void setUp() {
		mockStatic(ScriptValidator.class);
		mockDao();
		sc = loadFixture("sc", CompoundMetric.class);
		cbo = loadFixture("cbo", NativeMetric.class);
		lcom4 = loadFixture("lcom4", NativeMetric.class);
		configuration = loadFixture("sc", Configuration.class);
	}

	private void mockDao() {
		dao = mock(ConfigurationDao.class);
		mockStatic(DaoFactory.class);
		when(DaoFactory.getConfigurationDao()).thenReturn(dao);
	}

	@Test
	public void shouldImportFromFile() throws Exception {
		File file = mock(File.class);
		mockStatic(AbstractEntity.class);
		when(AbstractEntity.class, "importFrom", file, Configuration.class).thenReturn(configuration);
		assertSame(configuration, Configuration.importFrom(file));
	}

	@Test
	public void shouldExportToFile() throws Exception {
		File file = mock(File.class);
		mockStatic(FileUtils.class);

		configuration.exportTo(file);
		verifyStatic();
		FileUtils.writeStringToFile(file, loadResource("Configuration-sc.yml"));
	}

	@Test
	public void shouldGetAllConfigurations() {
		SortedSet<Configuration> configurations = mock(SortedSet.class);
		when(dao.all()).thenReturn(configurations);
		assertSame(configurations, Configuration.all());
	}

	@Test
	public void shouldSortByName() {
		assertSorted(withName("A"), withName("B"), withName("C"), withName("X"), withName("Y"), withName("Z"));
	}

	@Test
	public void shouldIdentifyByName() {
		assertEquals(configuration, withName(configuration.getName()));
	}

	private Configuration withName(String name) {
		return new Configuration(name);
	}

	@Test
	public void checkConstruction() {
		configuration = new Configuration();
		assertFalse(configuration.hasId());
		assertEquals("New configuration", configuration.getName());
		assertEquals("", configuration.getDescription());
		assertTrue(configuration.getMetricConfigurations().isEmpty());
	}

	@Test
	public void shouldSetConfigurationOnMetricConfigurations() {
		MetricConfiguration metricConfiguration = mock(MetricConfiguration.class);
		configuration.setMetricConfigurations(sortedSet(metricConfiguration));
		assertDeepEquals(set(metricConfiguration), configuration.getMetricConfigurations());
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
	public void shouldAddMetricConfigurationIfItDoesNotConflictWithExistingOnes() {
		configuration.addMetricConfiguration(new MetricConfiguration());
		assertAdd(new MetricConfiguration(new CompoundMetric("sc"))).throwsException()
			.withMessage("Metric with code 'sc' already exists in the configuration.");
		assertAdd(new MetricConfiguration(sc)).throwsException()
			.withMessage("Metric already exists in the configuration: Structural Complexity");
	}

	private TaskMatcher assertAdd(final MetricConfiguration metricConfiguration) {
		return assertThat(new VoidTask() {

			@Override
			protected void perform() throws Throwable {
				configuration.addMetricConfiguration(metricConfiguration);
			}
		});
	}

	@Test
	public void shouldRemoveMetricConfiguration() {
		MetricConfiguration metricConfiguration = mock(MetricConfiguration.class);
		SortedSet<MetricConfiguration> metricConfigurations = sortedSet(metricConfiguration);
		configuration.setMetricConfigurations(metricConfigurations);

		configuration.removeMetricConfiguration(metricConfiguration);
		assertTrue(configuration.getMetricConfigurations().isEmpty());
		verify(metricConfiguration).setConfiguration(null);
	}

	@Test
	public void shouldGetConfigurationForMetric() {
		assertDeepEquals(cbo, configuration.getConfigurationFor(cbo).getMetric());
		assertDeepEquals(lcom4, configuration.getConfigurationFor(lcom4).getMetric());
		assertDeepEquals(sc, configuration.getConfigurationFor(sc).getMetric());
		assertNull(configuration.getConfigurationFor(new CompoundMetric()));
	}

	@Test
	public void shouldGetCompoundMetrics() {
		assertDeepEquals(set(sc), configuration.getCompoundMetrics());
	}

	@Test
	public void shouldRetrieveNativeMetricsPerBaseTool() {
		BaseTool baseTool = loadFixture("inexistent", BaseTool.class);
		assertDeepEquals(map(baseTool, baseTool.getSupportedMetrics()), configuration.getNativeMetrics());
	}

	@Test
	public void shouldAssertSaved() {
		prepareSave(42L);

		configuration.assertSaved();
		verify(dao).save(configuration);

		configuration.assertSaved();
		verifyNoMoreInteractions(dao);
	}

	@Test
	public void shouldRequiredNameToSave() {
		configuration.setName(" ");
		assertThat(new VoidTask() {

			@Override
			protected void perform() {
				configuration.save();
			}
		}).throwsException().withMessage("Configuration requires name.");
	}

	@Test
	public void shouldValidateScriptsBeforeSave() {
		prepareSave(42L);
		configuration.save();
		verifyStatic();
		ScriptValidator.validate(configuration);
	}

	@Test
	public void shouldUpdateIdAndSaveMetricConfigurationsOnSave() {
		Long id = mock(Long.class);
		MetricConfiguration metricConfiguration = mock(MetricConfiguration.class);
		prepareSave(id, metricConfiguration);

		assertFalse(configuration.hasId());
		configuration.save();
		assertSame(id, configuration.getId());
		verify(metricConfiguration).save();
	}

	private void prepareSave(Long id, MetricConfiguration... metricConfigurations) {
		configuration.setMetricConfigurations(sortedSet(metricConfigurations));
		when(dao.save(configuration)).thenReturn(id);
	}

	@Test
	public void shouldIgnoreDeleteIfIsNotSaved() {
		configuration.delete();
		verify(dao, never()).delete(any(Long.class));
	}

	@Test
	public void shouldDeleteIfSaved() {
		Long id = mock(Long.class);
		Whitebox.setInternalState(configuration, "id", id);

		assertTrue(configuration.hasId());
		configuration.delete();
		assertFalse(configuration.hasId());
		verify(dao).delete(id);
	}

	@Test
	public void shouldNotifyMetricConfigurationsOfDeletion() {
		MetricConfiguration metricConfiguration = mock(MetricConfiguration.class);
		configuration.setMetricConfigurations(sortedSet(metricConfiguration));

		configuration.delete();
		verify(metricConfiguration).deleted();
	}

	@Test
	public void toStringShouldBeName() {
		assertEquals(configuration.getName(), "" + configuration);
	}
}