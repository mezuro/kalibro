package org.kalibro;

import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.Theory;
import org.kalibro.core.concurrent.TaskMatcher;
import org.kalibro.core.concurrent.VoidTask;
import org.kalibro.tests.AcceptanceTest;

public class MetricConfigurationAcceptanceTest extends AcceptanceTest {

	private Configuration configuration;
	private MetricConfiguration metricConfiguration;

	@Before
	public void setUp() {
		configuration = loadFixture("sc-analizo", Configuration.class);
		metricConfiguration = configuration.getMetricConfigurations().first();
	}

	@Theory
	public void testCrud(SupportedDatabase databaseType) {
		resetDatabase(databaseType);
		metricConfiguration.save();

		assertDeepEquals(metricConfiguration, saved());

		metricConfiguration.setCode("MetricConfigurationAcceptanceTestCode");
		assertFalse(metricConfiguration.deepEquals(saved()));

		metricConfiguration.save();
		assertDeepEquals(metricConfiguration, saved());

		metricConfiguration.delete();
		assertFalse(configuration.getMetricConfigurations().contains(metricConfiguration));
		assertFalse(Configuration.all().first().getMetricConfigurations().contains(metricConfiguration));
	}

	private MetricConfiguration saved() {
		return Configuration.all().first().getMetricConfigurations().first();
	}

	@Test
	public void metricConfigurationIsRequiredToBeInConfiguration() {
		assertSaveNew().throwsException().withMessage("Metric is not in any configuration.");
	}

	private TaskMatcher assertSaveNew() {
		return assertThat(new VoidTask() {

			@Override
			protected void perform() {
				new MetricConfiguration().save();
			}
		});
	}
}