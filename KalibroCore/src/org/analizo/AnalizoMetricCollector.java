package org.analizo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import org.kalibro.BaseTool;
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
	public BaseTool getBaseTool() {
		BaseTool baseTool = new BaseTool("Analizo");
		baseTool.setCollectorClass(AnalizoMetricCollector.class);
		baseTool.setSupportedMetrics(outputParser.getSupportedMetrics());
		return baseTool;
	}

	@Override
	public Set<NativeModuleResult> collectMetrics(File codeDirectory, Set<NativeMetric> metrics) throws IOException {
		String command = COMMAND + codeDirectory.getAbsolutePath();
		InputStream analizoOuput = new CommandTask(command).executeAndGetOuput();
		return outputParser.parseResults(analizoOuput, metrics);
	}
}