package org.cvsanaly;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.cvsanaly.entities.MetricResult;
import org.kalibro.*;
import org.kalibro.core.MetricCollector;
import org.kalibro.core.command.CommandTask;

public class CVSAnalyMetricCollector implements MetricCollector {

	private static final String CVSANALY2_COMMAND_LINE = "cvsanaly2 -q --extensions=Metrics --db-driver=sqlite -d ";

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
		File tempFile = File.createTempFile("kalibro-cvsanaly-db", ".sqlite");
		Set<NativeModuleResult> result;
		try {
			CommandTask executor = new CommandTask(CVSANALY2_COMMAND_LINE + tempFile.getAbsolutePath(), codeDirectory);
			executor.execute();

			CVSAnalyDatabaseFetcher databaseFetcher = new CVSAnalyDatabaseFetcher(tempFile);
			List<MetricResult> entities = databaseFetcher.getMetricResults();

			result = convertEntityToNativeModuleResult(entities, metrics);
		} finally {
			tempFile.delete();
		}
		return result;
	}

	private Set<NativeModuleResult> convertEntityToNativeModuleResult(List<MetricResult> entities,
		Set<NativeMetric> wantedMetrics) {

		Set<NativeModuleResult> result = new HashSet<NativeModuleResult>();

		for (MetricResult metric : entities) {
			String filename = metric.getFilePath();
			Module module = new Module(Granularity.CLASS, filename.split(Pattern.quote(File.separator)));
			NativeModuleResult nativeModuleResult = new NativeModuleResult(module);
			extractMetrics(metric, nativeModuleResult, wantedMetrics);

			result.add(nativeModuleResult);
		}
		return result;
	}

	private void extractMetrics(MetricResult entity, NativeModuleResult nativeModuleResult,
		Set<NativeMetric> wantedMetrics) {
		for (CVSAnalyMetric metric : CVSAnalyMetric.values()) {
			NativeMetric nativeMetric = metric.getNativeMetric();
			if (wantedMetrics.contains(nativeMetric))
				nativeModuleResult.addMetricResult(new NativeMetricResult(nativeMetric, metric.getMetricValue(entity)));
		}
	}

}
