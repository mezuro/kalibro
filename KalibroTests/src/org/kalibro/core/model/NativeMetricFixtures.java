package org.kalibro.core.model;

import java.util.HashSet;
import java.util.Set;

import org.analizo.AnalizoStub;

public final class NativeMetricFixtures {

	public static NativeMetric nativeMetric(String code) {
		return copyAndSetOrigin(AnalizoStub.nativeMetric(code));
	}

	public static Set<NativeMetric> nativeMetrics() {
		Set<NativeMetric> nativeMetrics = new HashSet<NativeMetric>();
		for (NativeMetric metric : AnalizoStub.nativeMetrics())
			nativeMetrics.add(copyAndSetOrigin(metric));
		return nativeMetrics;
	}

	public static NativeMetric copyAndSetOrigin(NativeMetric original) {
		NativeMetric metric = new NativeMetric(original.getName(), original.getScope(), original.getLanguages());
		metric.setOrigin("Analizo");
		return metric;
	}

	private NativeMetricFixtures() {
		// Utility class
	}
}