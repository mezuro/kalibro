package org.cvsanaly;

import java.io.File;
import java.util.Set;

import org.kalibro.core.MetricCollector;
import org.kalibro.core.model.BaseTool;
import org.kalibro.core.model.NativeMetric;
import org.kalibro.core.model.NativeModuleResult;


public class CVSAnalyMetricCollector implements MetricCollector {

	@Override
	public BaseTool getBaseTool() {
		BaseTool baseTool = new BaseTool("CVSAnaly");
		baseTool.setCollectorClass(CVSAnalyMetricCollector.class);
		for (CVSAnalyMetric metric : CVSAnalyMetric.values())
			baseTool.addSupportedMetric(metric.getNativeMetric());
		return baseTool;
	}

	@Override
	public Set<NativeModuleResult> collectMetrics(File codeDirectory, Set<NativeMetric> metrics) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
