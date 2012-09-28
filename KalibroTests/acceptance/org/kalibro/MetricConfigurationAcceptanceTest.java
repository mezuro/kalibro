package org.kalibro;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.kalibro.core.concurrent.VoidTask;
import org.kalibro.tests.AcceptanceTest;

@RunWith(Parameterized.class)
public class MetricConfigurationAcceptanceTest extends AcceptanceTest {

	private MetricConfiguration metricConfiguration;
	private Configuration configuration;

	public MetricConfigurationAcceptanceTest(SupportedDatabase databaseType) {
		super(databaseType);
	}

	@Before
	public void setUp() {
		configuration = loadFixture("analizo", Configuration.class);
		configuration.save();
		metricConfiguration = configuration.getMetricConfigurations().first();
	}

	@After
	public void tearDown() {
		configuration.delete();
	}

	@Test
	public void testCrud() {
		assertSaved();

		metricConfiguration.setWeight(0.0);
		assertDifferentFromSaved();

		metricConfiguration.save();
		assertSaved();

		metricConfiguration.delete();
		assertFalse(configuration.getMetricConfigurations().contains(metricConfiguration));
		assertFalse(Configuration.all().first().getMetricConfigurations().contains(metricConfiguration));
	}

	private void assertSaved() {
		assertDeepEquals(metricConfiguration, ReadingGroup.all().first().getReadings().first());
	}

	private void assertDifferentFromSaved() {
		Reading saved = ReadingGroup.all().first().getReadings().first();
		assertFalse(metricConfiguration.deepEquals(saved));
	}

	@Test
	public void metricConfigurationIsRequiredToBeInConfiguration() {
		assertThat(saveNew()).throwsException().withMessage("Metric configuration is not in any configuration.");
	}

	private VoidTask saveNew() {
		return new VoidTask() {

			@Override
			protected void perform() {
				new MetricConfiguration().save();
			}
		};
	}

	@Test
	public void shouldNotSetConflictingCode() {
		assertThat(new VoidTask() {

			@Override
			protected void perform() throws Throwable {
				metricConfiguration.setCode("loc");
			}
		}).throwsException().withMessage("Metric configuration with code loc already exists in the configuration.");
		assertEquals("acc", metricConfiguration.getCode());
	}
}