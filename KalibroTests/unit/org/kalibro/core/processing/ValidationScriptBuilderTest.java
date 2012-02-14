package org.kalibro.core.processing;

import static org.junit.Assert.*;
import static org.kalibro.core.model.CompoundMetricFixtures.*;
import static org.kalibro.core.model.MetricConfigurationFixtures.*;

import org.junit.Test;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.model.Configuration;
import org.kalibro.core.model.MetricConfiguration;

public class ValidationScriptBuilderTest extends KalibroTestCase {

	@Test(timeout = UNIT_TIMEOUT)
	public void testScript() {
		ValidationScriptBuilder scriptBuilder = new ValidationScriptBuilder(createConfiguration(), sc());
		String expected = "var cbo = 1.0;\nvar lcom4 = 1.0;\nfunction sc(){return cbo * lcom4;}\n";
		assertEquals(expected, scriptBuilder.buildScript());
	}

	private Configuration createConfiguration() {
		Configuration configuration = new Configuration();
		configuration.addMetricConfiguration(configuration("cbo"));
		configuration.addMetricConfiguration(configuration("lcom4"));
		configuration.addMetricConfiguration(createScConfiguration());
		return configuration;
	}

	private MetricConfiguration createScConfiguration() {
		MetricConfiguration scConfiguration = new MetricConfiguration(sc());
		scConfiguration.setCode("sc");
		return scConfiguration;
	}
}