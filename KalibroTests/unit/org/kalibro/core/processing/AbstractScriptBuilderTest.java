package org.kalibro.core.processing;

import static org.junit.Assert.*;
import static org.kalibro.core.model.ConfigurationFixtures.*;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.model.CompoundMetric;
import org.kalibro.core.model.Configuration;
import org.kalibro.core.model.Metric;
import org.kalibro.core.model.MetricConfiguration;

public class AbstractScriptBuilderTest extends KalibroTestCase {

	private AbstractScriptBuilder scriptBuilder;

	@Before
	public void setUp() {
		Configuration configuration = kalibroConfiguration();
		CompoundMetric compoundMetric = new CompoundMetric();
		compoundMetric.setName("Another");
		compoundMetric.setScript("return 42;");
		configuration.addMetricConfiguration(new MetricConfiguration(compoundMetric));
		scriptBuilder = new MyScriptBuilder(configuration);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testScript() {
		assertEquals("var acc = 72.0;\nvar accm = 40.0;\nvar amloc = 18.0;\nfunction another(){return 42;}\n",
			scriptBuilder.buildScript());
	}

	private class MyScriptBuilder extends AbstractScriptBuilder {

		public MyScriptBuilder(Configuration configuration) {
			super(configuration);
		}

		@Override
		protected boolean shouldInclude(Metric metric) {
			return metric.getName().startsWith("A");
		}

		@Override
		protected Double getValueFor(Metric metric) {
			return new Double(metric.getName().length());
		}
	}
}