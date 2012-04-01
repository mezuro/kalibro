package org.kalibro.core.processing;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.model.CompoundMetricFixtures;
import org.kalibro.core.model.Configuration;
import org.kalibro.core.model.ConfigurationFixtures;
import org.kalibro.core.model.MetricConfiguration;

public class ValidationScriptBuilderTest extends KalibroTestCase {

	private MetricConfiguration scConfiguration;
	private Configuration configuration;

	@Before
	public void setUp() {
		scConfiguration = new MetricConfiguration(CompoundMetricFixtures.sc());
		scConfiguration.setCode("sc");
		configuration = ConfigurationFixtures.simpleConfiguration();
		configuration.addMetricConfiguration(scConfiguration);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testScript() {
		ValidationScriptBuilder scriptBuilder = new ValidationScriptBuilder(configuration, scConfiguration);
		List<String> expected = Arrays.asList("var amloc = 1.0;", "var cbo = 1.0;", "var lcom4 = 1.0;",
			"function sc(){return cbo * lcom4;}");
		assertDeepEquals(expected, scriptBuilder.buildScript().split("\\n"));
	}
}