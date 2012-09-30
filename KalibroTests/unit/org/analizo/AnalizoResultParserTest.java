package org.analizo;

import static org.kalibro.Granularity.*;
import static org.kalibro.Language.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.Module;
import org.kalibro.NativeMetric;
import org.kalibro.NativeMetricResult;
import org.kalibro.NativeModuleResult;
import org.kalibro.tests.UnitTest;

public class AnalizoResultParserTest extends UnitTest {

	private Map<NativeMetric, String> supportedMetrics;
	private NativeMetric totalLoc, cbo;
	private InputStream input;

	@Before
	public void setUp() throws IOException {
		supportedMetrics = new AnalizoMetricListParser(getStream("analizo-metrics-list")).getSupportedMetrics();
		totalLoc = new NativeMetric("Total Lines of Code", SOFTWARE, C, CPP, JAVA);
		cbo = new NativeMetric("Coupling Between Objects", CLASS, C, CPP, JAVA);
		input = getStream("analizo-metrics-HelloWorld");
	}

	@Test
	public void shouldParseAnalizoOutputToModuleResults() {
		AnalizoResultParser parser = new AnalizoResultParser(supportedMetrics, asSet(totalLoc, cbo));
		assertDeepEquals(expectedResults(), parser.parse(input));
	}

	private Set<NativeModuleResult> expectedResults() {
		Set<NativeModuleResult> expectedResults = new HashSet<NativeModuleResult>();
		expectedResults.add(moduleResult(totalLoc, 4.0, "null"));
		expectedResults.add(moduleResult(cbo, 0.0, "org", "HelloWorld"));
		return expectedResults;
	}

	private NativeModuleResult moduleResult(NativeMetric metric, double value, String... moduleName) {
		NativeModuleResult moduleResult = new NativeModuleResult(new Module(metric.getScope(), moduleName));
		moduleResult.addMetricResult(new NativeMetricResult(metric, value));
		return moduleResult;
	}
}