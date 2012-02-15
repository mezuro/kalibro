package org.checkstyle;

import static org.checkstyle.CheckstyleMetric.*;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.kalibro.core.MetricCollector;
import org.kalibro.core.model.Module;
import org.kalibro.core.model.NativeMetric;
import org.kalibro.core.model.NativeMetricResult;
import org.kalibro.core.model.NativeModuleResult;
import org.kalibro.core.model.enums.Granularity;

public class CheckstyleStub implements MetricCollector {

	private static NativeModuleResult result = initializeResult();

	private static NativeModuleResult initializeResult() {
		Module module = new Module(Granularity.CLASS, "HelloWorld");
		result = new NativeModuleResult(module);
		result.addMetricResult(new NativeMetricResult(NUMBER_OF_METHODS.getNativeMetric(), 1.0));
		result.addMetricResult(new NativeMetricResult(FILE_LENGTH.getNativeMetric(), 6.0));
		return result;
	}

	public static NativeModuleResult result() {
		return result;
	}
	
	public static Set<NativeModuleResult> results() {
		return new HashSet<NativeModuleResult>(Arrays.asList(result));
	}

	@Override
	public Set<NativeMetric> getSupportedMetrics() {
		return CheckstyleMetric.supportedMetrics();
	}

	@Override
	public Set<NativeModuleResult> collectMetrics(File codeDirectory, Set<NativeMetric> metrics) {
		return results();
	}
}