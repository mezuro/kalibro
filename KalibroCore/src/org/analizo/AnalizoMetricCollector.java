package org.analizo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.kalibro.MetricCollector;
import org.kalibro.NativeMetric;
import org.kalibro.NativeModuleResult;
import org.kalibro.core.command.CommandTask;

/**
 * Metric collector for Analizo.
 * 
 * @author Carlos Morais
 */
public class AnalizoMetricCollector implements MetricCollector {

	private String description;
	private Map<NativeMetric, String> supportedMetrics;

	public AnalizoMetricCollector() throws IOException {
		description = IOUtils.toString(getClass().getResourceAsStream("description"));
		supportedMetrics = new AnalizoMetricListParser(executeAnalizo("--list")).getSupportedMetrics();
	}

	@Override
	public String name() {
		return "Analizo";
	}

	@Override
	public String description() {
		return description;
	}

	@Override
	public Set<NativeMetric> supportedMetrics() {
		return supportedMetrics.keySet();
	}

	@Override
	public Set<NativeModuleResult> collectMetrics(File codeDirectory, Set<NativeMetric> wantedMetrics)
		throws IOException {
		InputStream analizoOutput = executeAnalizo(codeDirectory.getAbsolutePath());
		return new AnalizoResultParser(supportedMetrics, wantedMetrics).parse(analizoOutput);
	}

	private InputStream executeAnalizo(String argument) throws IOException {
		return new CommandTask("analizo metrics " + argument).executeAndGetOuput();
	}
}