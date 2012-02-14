package org.analizo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import org.kalibro.core.MetricCollector;
import org.kalibro.core.command.CommandExecutor;
import org.kalibro.core.model.NativeMetric;
import org.kalibro.core.model.NativeModuleResult;

public class AnalizoMetricCollector implements MetricCollector {

	private static final String COMMAND = "analizo metrics ";

	private AnalizoOutputParser outputParser;

	public AnalizoMetricCollector() throws IOException {
		InputStream metricListOutput = new CommandExecutor(COMMAND + "--list").executeCommandAndGetOuput();
		outputParser = new AnalizoOutputParser(metricListOutput);
	}

	@Override
	public Set<NativeMetric> getSupportedMetrics() {
		return outputParser.getSupportedMetrics();
	}

	@Override
	public Set<NativeModuleResult> collectMetrics(File codeDirectory, Set<NativeMetric> metrics) {
		String command = COMMAND + codeDirectory.getAbsolutePath();
		InputStream analizoOuput = new CommandExecutor(command).executeCommandAndGetOuput();
		return outputParser.parseResults(analizoOuput, metrics);
	}
}