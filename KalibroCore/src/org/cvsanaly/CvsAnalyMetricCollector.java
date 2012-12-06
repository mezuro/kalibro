package org.cvsanaly;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.cvsanaly.entities.MetricResult;
import org.kalibro.*;
import org.kalibro.core.command.CommandTask;
import org.kalibro.core.concurrent.Writer;

public class CvsAnalyMetricCollector implements MetricCollector {

	private static final String CVSANALY2_COMMAND_LINE = "cvsanaly2 -q --extensions=Metrics --db-driver=sqlite -d ";

	@Override
	public String name() {
		return "CVSAnalY";
	}

	@Override
	public String description() {
		return "";
	}

	@Override
	public Set<NativeMetric> supportedMetrics() {
		Set<NativeMetric> supportedMetrics = new HashSet<NativeMetric>();
		for (CvsAnalyMetric metric : CvsAnalyMetric.values())
			supportedMetrics.add(metric.getNativeMetric());
		return supportedMetrics;
	}

	@Override
	public void collectMetrics(
		File codeDirectory, Set<NativeMetric> wantedMetrics, Writer<NativeModuleResult> resultWriter) throws Exception {
		File tempFile = File.createTempFile("kalibro-cvsanaly-db", ".sqlite");
		try {
			CommandTask executor = new CommandTask(CVSANALY2_COMMAND_LINE + tempFile.getAbsolutePath(), codeDirectory);
			executor.execute();

			CvsAnalyDatabaseFetcher databaseFetcher = new CvsAnalyDatabaseFetcher(tempFile);
			List<MetricResult> entities = databaseFetcher.getMetricResults();

			convertEntityToNativeModuleResult(entities, wantedMetrics, resultWriter);
		} finally {
			tempFile.delete();
		}
		resultWriter.close();
	}

	private void convertEntityToNativeModuleResult(List<MetricResult> entities,
		Set<NativeMetric> wantedMetrics, Writer<NativeModuleResult> resultWriter) {
		for (MetricResult metric : entities) {
			String filename = metric.getFilePath();
			Module module = new Module(Granularity.CLASS, filename.split(Pattern.quote(File.separator)));
			NativeModuleResult nativeModuleResult = new NativeModuleResult(module);
			extractMetrics(metric, nativeModuleResult, wantedMetrics);

			resultWriter.write(nativeModuleResult);
		}
	}

	private void extractMetrics(MetricResult entity, NativeModuleResult nativeModuleResult,
		Set<NativeMetric> wantedMetrics) {
		for (CvsAnalyMetric metric : CvsAnalyMetric.values()) {
			NativeMetric nativeMetric = metric.getNativeMetric();
			if (wantedMetrics.contains(nativeMetric))
				nativeModuleResult.addMetricResult(new NativeMetricResult(nativeMetric, metric.getMetricValue(entity)));
		}
	}
}