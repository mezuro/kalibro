package org.analizo;

import static org.analizo.AnalizoStub.*;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;

public class AnalizoOutputParserTest extends KalibroTestCase {

	private AnalizoOutputParser parser;

	@Before
	public void setUp() throws IOException {
		InputStream metricListOutput = getResource("Analizo-Output-MetricList.txt");
		parser = new AnalizoOutputParser(metricListOutput);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldParseMetricListOutputToSupportedMetrics() {
		assertDeepEquals(nativeMetrics(), parser.getSupportedMetrics());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldParseResultsOutputToModuleResults() {
		InputStream resultsOutput = getResource("Analizo-Output-HelloWorld.txt");
		assertDeepEquals(collectMetrics(), parser.parseResults(resultsOutput, nativeMetrics()));
	}

	private InputStream getResource(String resourceName) {
		return getClass().getResourceAsStream(resourceName);
	}
}