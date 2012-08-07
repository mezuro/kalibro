package org.cvsanaly;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.cvsanaly.entities.MetricResult;
import org.kalibro.core.MetricCollector;
import org.kalibro.core.command.CommandTask;
import org.kalibro.core.model.*;
import org.kalibro.core.model.enums.Granularity;

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
		// TODO Only collect the given metrics
		File tempFile = File.createTempFile("kalibro-cvsanaly-db", ".sqlite");
		Set<NativeModuleResult> result;
		try {
			CommandTask executor = new CommandTask(CVSANALY2_COMMAND_LINE + tempFile.getAbsolutePath(), codeDirectory);
			executor.executeAndWait();

			CVSAnalyDatabaseFetcher databaseFetcher = new CVSAnalyDatabaseFetcher(tempFile);
			List<MetricResult> entities = databaseFetcher.getMetricResults();

			result = convertEntityToNativeModuleResult(entities);
		} finally {
			tempFile.delete();
		}
		return result;
	}

	private Set<NativeModuleResult> convertEntityToNativeModuleResult(List<MetricResult> entities) {
		Set<NativeModuleResult> result = new HashSet<NativeModuleResult>();

		for (MetricResult entity : entities) {
			// TODO Modify CVSAnaly to get information about path
			String filename = entity.getFile().getFilename();
			Module module = new Module(Granularity.CLASS, filename);
			NativeModuleResult nativeModuleResult = new NativeModuleResult(module);
			extractMetrics(entity, nativeModuleResult);

			result.add(nativeModuleResult);
		}
		return result;
	}

	private void extractMetrics(MetricResult entity, NativeModuleResult nativeModuleResult) {
		for (CVSAnalyMetric metric : CVSAnalyMetric.values()) {
			nativeModuleResult.addMetricResult(new NativeMetricResult(metric.getNativeMetric(),
				metric.getMetricValue(entity)));
		}
	}

}
