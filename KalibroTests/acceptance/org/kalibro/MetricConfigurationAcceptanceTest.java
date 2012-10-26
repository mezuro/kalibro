package org.kalibro;

import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.Theory;
import org.kalibro.core.concurrent.TaskMatcher;
import org.kalibro.core.concurrent.VoidTask;
import org.kalibro.tests.AcceptanceTest;

public class MetricConfigurationAcceptanceTest extends AcceptanceTest {

	private MetricConfiguration metricConfiguration;
	private Configuration configuration;

	@Before
	public void setUp() {
		configuration = loadFixture("sc-analizo", Configuration.class);
	}

	@Theory
	public void testCrud(SupportedDatabase databaseType) {
		resetDatabase(databaseType);
		configuration.save();
		metricConfiguration = configuration.getMetricConfigurations().first();

		assertDeepEquals(metricConfiguration, saved());

		metricConfiguration.setWeight(0.0);
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