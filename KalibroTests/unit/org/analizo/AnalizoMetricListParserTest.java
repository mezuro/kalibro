package org.analizo;

import static org.kalibro.Granularity.*;
import static org.kalibro.Language.*;

import java.io.IOException;

import org.junit.Test;
import org.kalibro.NativeMetric;
import org.kalibro.tests.UnitTest;

public class AnalizoMetricListParserTest extends UnitTest {

	@Test
	public void shouldParseMetricListToSupportedMetrics() throws IOException {
		assertDeepEquals(asMap(
			new NativeMetric("Total Coupling Factor", SOFTWARE, C, CPP, JAVA), "total_cof",
			new NativeMetric("Total Lines of Code", SOFTWARE, C, CPP, JAVA), "total_loc",
			new NativeMetric("Coupling Between Objects", CLASS, C, CPP, JAVA), "cbo",
			new NativeMetric("Lack of Cohesion of Methods", CLASS, C, CPP, JAVA), "lcom4"),
			new AnalizoMetricListParser(getStream("metrics-list")).getSupportedMetrics());
	}
}