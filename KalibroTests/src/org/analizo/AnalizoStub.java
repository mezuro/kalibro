package org.analizo;

import static org.kalibro.MetricFixtures.*;
import static org.kalibro.NativeModuleResultFixtures.*;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.kalibro.MetricCollector;
import org.kalibro.NativeMetric;
import org.kalibro.NativeModuleResult;

public class AnalizoStub implements MetricCollector {

	@Override
	public String name() {
		return "Analizo";
	}

	@Override
	public String description() {
		return "Analizo is a suite of source code analysis tools, aimed at being language-independent and " +
			"extensible. Analizo Metrics the tool which analyzes source code in the directories passed as arguments " +
			"and procudes a metrics report written to the standard output in the YAML format.";
	}

	@Override
	public Set<NativeMetric> supportedMetrics() {
		Set<NativeMetric> supportedMetrics = new HashSet<NativeMetric>();
		for (String code : analizoMetricCodes())
			supportedMetrics.add(newAnalizoMetric(code));
		return supportedMetrics;
	}

	@Override
	public Set<NativeModuleResult> collectMetrics(File codeDirectory, Set<NativeMetric> metrics) {
		HashSet<NativeModuleResult> results = new HashSet<NativeModuleResult>();
		results.add(newHelloWorldApplicationResult());
		results.add(newHelloWorldClassResult());
		return results;
	}
}