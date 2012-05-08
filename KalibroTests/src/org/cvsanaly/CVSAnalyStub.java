package org.cvsanaly;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.kalibro.core.model.BaseTool;
import org.kalibro.core.model.NativeMetric;
import org.kalibro.core.model.enums.Granularity;
import org.kalibro.core.model.enums.Language;

public final class CVSAnalyStub {

	private CVSAnalyStub() {}

	public static BaseTool getBaseTool() {
		BaseTool baseTool = new BaseTool("CVSAnaly");
		baseTool.setCollectorClass(CVSAnalyMetricCollector.class);
		for (NativeMetric metric : getSupportedMetrics())
			baseTool.addSupportedMetric(metric);
		return baseTool;
	}

	public static Set<NativeMetric> getSupportedMetrics() {
		NativeMetric[] metric = {
			new NativeMetric("Number of source lines of code", Granularity.CLASS, Language.values()),
			new NativeMetric("Number of lines of code", Granularity.CLASS, Language.values()),
			new NativeMetric("Number of comments", Granularity.CLASS, Language.values()),
			new NativeMetric("Number of commented lines", Granularity.CLASS, Language.values()),
			new NativeMetric("Number of blank lines", Granularity.CLASS, Language.values()),
			new NativeMetric("Number of functions", Granularity.CLASS, Language.values()),
		};
		return new HashSet<NativeMetric>(Arrays.asList(metric));
	}
}