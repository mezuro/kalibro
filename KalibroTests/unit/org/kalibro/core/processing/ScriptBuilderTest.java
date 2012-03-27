package org.kalibro.core.processing;

import static org.junit.Assert.*;
import static org.kalibro.core.model.CompoundMetricFixtures.*;
import static org.kalibro.core.model.MetricConfigurationFixtures.*;
import static org.kalibro.core.model.ModuleResultFixtures.*;
import static org.kalibro.core.model.NativeMetricFixtures.*;
import static org.kalibro.core.model.enums.Granularity.*;
import static org.kalibro.core.model.enums.Language.*;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.model.CompoundMetric;
import org.kalibro.core.model.Configuration;
import org.kalibro.core.model.MetricConfiguration;
import org.kalibro.core.model.NativeMetric;

public class ScriptBuilderTest extends KalibroTestCase {

	private CompoundMetric sc;
	private ScriptBuilder scriptBuilder;

	@Before
	public void setUp() {
		sc = sc();
		scriptBuilder = new ScriptBuilder(createConfiguration(), helloWorldClassResult(), sc);
	}

	private Configuration createConfiguration() {
		Configuration configuration = new Configuration();
		configuration.addMetricConfiguration(configuration("cbo"));
		configuration.addMetricConfiguration(configuration("lcom4"));
		configuration.addMetricConfiguration(scConfiguration());
		return configuration;
	}

	private MetricConfiguration scConfiguration() {
		MetricConfiguration scConfiguration = new MetricConfiguration(sc);
		scConfiguration.setCode("sc");
		return scConfiguration;
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldBuildScript() {
		String expected = "var cbo = 0.0;\nvar lcom4 = 1.0;\nfunction sc(){return cbo * lcom4;}\n";
		assertEquals(expected, scriptBuilder.buildScript());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldIncludeNativeMetricsFromModuleResult() {
		assertTrue(scriptBuilder.shouldInclude(nativeMetric("amloc")));
		assertTrue(scriptBuilder.shouldInclude(nativeMetric("loc")));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldIncludeJustTheEvaluatedCompoundMetric() {
		assertTrue(scriptBuilder.shouldInclude(sc));
		assertFalse(scriptBuilder.shouldInclude(new CompoundMetric()));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotIncludeMetricIfThereIsNoResultForIt() {
		assertFalse(scriptBuilder.shouldInclude(new NativeMetric("ScriptBuilderTest metric", METHOD, JAVA)));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotIncludeMetricIfScopeIsNotCompatible() {
		NativeMetric cbo = nativeMetric("cbo");
		assertTrue(scriptBuilder.shouldInclude(cbo));

		cbo.setScope(APPLICATION);
		assertFalse(scriptBuilder.shouldInclude(cbo));
	}
}