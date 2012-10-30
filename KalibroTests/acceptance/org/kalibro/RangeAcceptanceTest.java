package org.kalibro;

import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.Theory;
import org.kalibro.core.concurrent.TaskMatcher;
import org.kalibro.core.concurrent.VoidTask;
import org.kalibro.tests.AcceptanceTest;

public class RangeAcceptanceTest extends AcceptanceTest {

	private NativeMetric lcom4;
	private Configuration configuration;
	private MetricConfiguration metricConfiguration;

	private Range range;

	@Before
	public void setUp() {
		lcom4 = loadFixture("lcom4", NativeMetric.class);
		configuration = loadFixture("sc-analizo", Configuration.class);
		metricConfiguration = configuration.getConfigurationFor(lcom4);
		range = metricConfiguration.getRangeFor(0.0);
	}

	@Theory
	public void testCrud(SupportedDatabase databaseType) {
		resetDatabase(databaseType);
		range.save();

		assertDeepEquals(range, saved());

		range.setBeginning(Double.NEGATIVE_INFINITY);
		assertFalse(range.deepEquals(saved()));

		range.save();
		assertDeepEquals(range, saved());

		range.delete();
		assertFalse(metricConfiguration.getRanges().contains(range));
		assertFalse(Configuration.all().first().getConfigurationFor(lcom4).getRanges().contains(range));
	}

	private Range saved() {
		return Configuration.all().first().getConfigurationFor(lcom4).getRangeFor(0.0);
	}

	@Test
	public void rangeIsRequiredToBeInMetricConfiguration() {
		assertSaveNew().throwsException().withMessage("Range is not in any configuration.");
	}

	private TaskMatcher assertSaveNew() {
		return assertThat(new VoidTask() {

			@Override
			protected void perform() {
				new Range().save();
			}
		});
	}
}