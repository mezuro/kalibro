package org.analizo;

import static org.kalibro.core.model.BaseToolFixtures.*;
import static org.kalibro.core.model.NativeModuleResultFixtures.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.model.NativeMetric;

public class AnalizoOutputParserTest extends KalibroTestCase {

	private AnalizoOutputParser parser;

	@Before
	public void setUp() throws IOException {
		InputStream metricListOutput = getResource("Analizo-Output-MetricList.txt");
		parser = new AnalizoOutputParser(metricListOutput);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldParseMetricListOutputToSupportedMetrics() {
		assertDeepEquals(analizo().getSupportedMetrics(), parser.getSupportedMetrics());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldParseResultsOutputToModuleResults() {
		InputStream resultsOutput = getResource("Analizo-Output-HelloWorld.txt");
		Set<NativeMetric> metrics = analizo().getSupportedMetrics();
		assertDeepEquals(parser.parseResults(resultsOutput, metrics),
			helloWorldApplicationResult(), helloWorldClassResult());
	}

	private InputStream getResource(String resourceName) {
		return getClass().getResourceAsStream(resourceName);
	}
}