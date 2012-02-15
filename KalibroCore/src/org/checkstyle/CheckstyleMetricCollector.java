package org.checkstyle;

import java.io.File;
import java.util.Set;

import org.kalibro.core.MetricCollector;
import org.kalibro.core.model.NativeMetric;
import org.kalibro.core.model.NativeModuleResult;

public class CheckstyleMetricCollector implements MetricCollector {

	@Override
	public Set<NativeMetric> getSupportedMetrics() {
		return CheckstyleMetric.supportedMetrics();
	}

	@Override
	public Set<NativeModuleResult> collectMetrics(File codeDirectory, Set<NativeMetric> metrics) throws Exception {
		//TODO Filter using the metrics argument
		CheckstyleOutputParser parser = new CheckstyleOutputParser();
		new KalibroChecker(parser).process(codeDirectory);
		return parser.getResults();
	}
}