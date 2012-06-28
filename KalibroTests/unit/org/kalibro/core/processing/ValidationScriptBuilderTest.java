package org.kalibro.core.processing;

import static org.junit.Assert.*;
import static org.kalibro.core.model.ConfigurationFixtures.*;
import static org.kalibro.core.model.MetricConfigurationFixtures.*;
import static org.kalibro.core.model.MetricFixtures.*;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.model.Configuration;
import org.kalibro.core.model.MetricConfiguration;

public class ValidationScriptBuilderTest extends KalibroTestCase {

	private MetricConfiguration scConfiguration;
	private Configuration configuration;

	@Before
	public void setUp() {
		scConfiguration = new MetricConfiguration(sc());
		scConfiguration.setCode("sc");
		configuration = newConfiguration("cbo", "lcom4");
		configuration.addMetricConfiguration(scConfiguration);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testCompoundScript() {
		ValidationScriptBuilder scriptBuilder = new ValidationScriptBuilder(configuration, scConfiguration);
		String expected = "var cbo = 1.0;\nvar lcom4 = 1.0;\nfunction sc(){return cbo * lcom4;}\n";
		assertEquals(expected, scriptBuilder.buildScript());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testNativeScript() {
		ValidationScriptBuilder scriptBuilder = new ValidationScriptBuilder(configuration, metricConfiguration("cbo"));
		assertEquals("function cbo(){return 1.0;}", scriptBuilder.buildScript());
	}
}