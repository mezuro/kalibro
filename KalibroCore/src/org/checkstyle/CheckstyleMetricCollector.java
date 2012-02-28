package org.checkstyle;

import com.puppycrawl.tools.checkstyle.api.Configuration;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.kalibro.core.MetricCollector;
import org.kalibro.core.model.NativeMetric;
import org.kalibro.core.model.NativeModuleResult;

public class CheckstyleMetricCollector implements MetricCollector {

	@Override
	public Set<NativeMetric> getSupportedMetrics() {
		Set<NativeMetric> supportedMetrics = new HashSet<NativeMetric>();
		for (CheckstyleMetric metric : CheckstyleMetric.values())
			supportedMetrics.add(metric.getNativeMetric());
		return supportedMetrics;
	}

	@Override
	public Set<NativeModuleResult> collectMetrics(File codeDirectory, Set<NativeMetric> metrics) throws Exception {
		CheckstyleOutputParser parser = new CheckstyleOutputParser();
		Configuration configuration = CheckstyleConfiguration.checkerConfiguration(metrics);
		new KalibroChecker(parser, configuration).process(codeDirectory);
		return parser.getResults();
	}
}