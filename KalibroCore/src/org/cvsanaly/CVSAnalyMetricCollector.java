package org.cvsanaly;

import java.io.File;
import java.util.List;
import java.util.Set;

import org.cvsanaly.entities.MetricResult;
import org.kalibro.core.MetricCollector;
import org.kalibro.core.command.CommandTask;
import org.kalibro.core.model.BaseTool;
import org.kalibro.core.model.NativeMetric;
import org.kalibro.core.model.NativeModuleResult;

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
		CommandTask executor = new CommandTask(CVSANALY2_COMMAND_LINE + tempFile.getAbsolutePath(), codeDirectory);
		executor.executeAndWait();
		
		CVSAnalyDatabaseFetcher databaseFetcher = new CVSAnalyDatabaseFetcher(tempFile);
		List<MetricResult> entites = databaseFetcher.getMetricResults();
		
		tempFile.delete();
		return null;
	}
	
}
