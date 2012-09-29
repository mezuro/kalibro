package org.analizo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import org.kalibro.MetricCollector;
import org.kalibro.NativeMetric;
import org.kalibro.NativeModuleResult;
import org.kalibro.core.command.CommandTask;

public class AnalizoMetricCollector implements MetricCollector {

	private static final String COMMAND = "analizo metrics ";

	private AnalizoOutputParser outputParser;

	public AnalizoMetricCollector() throws IOException {
		InputStream metricListOutput = new CommandTask(COMMAND + "--list").executeAndGetOuput();
		outputParser = new AnalizoOutputParser(metricListOutput);
	}

	@Override
	public String name() {
		return "Analizo";
	}

	@Override
	public String description() {
		return "Analizo is a suite of source code analysis tools, aimed at being language-independent and " +
			"extensible. Analizo Metrics the tool which analyzes source code in the directories passed as arguments " +
			"and procudes a metrics report written to the standard output in the YAML format.";
	}

	@Override
	public Set<NativeMetric> supportedMetrics() {
		return outputParser.getSupportedMetrics();
	}

	@Override
	public Set<NativeModuleResult> collectMetrics(File codeDirectory, Set<NativeMetric> metrics) throws IOException {
		String command = COMMAND + codeDirectory.getAbsolutePath();
		InputStream analizoOuput = new CommandTask(command).executeAndGetOuput();
		return outputParser.parseResults(analizoOuput, metrics);
	}
}