package org.cvsanaly;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.kalibro.MetricCollector;
import org.kalibro.NativeMetric;
import org.kalibro.NativeModuleResult;
import org.kalibro.core.command.CommandTask;
import org.kalibro.core.concurrent.Writer;

/**
 * Metric collector for CVSAnalY.
 * 
 * @author Carlos Morais
 * @author Eduardo Morais
 */
public class CvsAnalyMetricCollector implements MetricCollector {

	private static final String COMMAND = "cvsanaly2 -q --extensions=Metrics --db-driver=sqlite -d ";

	private String description;

	public CvsAnalyMetricCollector() throws IOException {
		new CommandTask("cvsanaly2 --version").execute();
		description = IOUtils.toString(getClass().getResourceAsStream("description"));
	}

	@Override
	public String name() {
		return "CVSAnalY";
	}

	@Override
	public String description() {
		return description;
	}

	@Override
	public Set<NativeMetric> supportedMetrics() {
		return new HashSet<NativeMetric>(CvsAnalyMetric.supportedMetrics());
	}

	@Override
	public void collectMetrics(
		File codeDirectory, Set<NativeMetric> wantedMetrics, Writer<NativeModuleResult> resultWriter) throws Exception {
		File databaseFile = File.createTempFile("kalibro-cvsanaly-db", ".sqlite");
		new CommandTask(COMMAND + databaseFile.getAbsolutePath(), codeDirectory).execute();
		new CvsAnalyDatabaseFetcher(wantedMetrics).queryMetrics(databaseFile, resultWriter);
	}
}