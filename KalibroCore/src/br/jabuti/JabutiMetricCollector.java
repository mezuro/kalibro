package br.jabuti;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import br.jabuti.JabutiOutputParser;
import org.kalibro.core.MetricCollector;
import org.kalibro.core.command.CommandTask;
import org.kalibro.core.model.BaseTool;
import org.kalibro.core.model.NativeMetric;
import org.kalibro.core.model.NativeModuleResult;

public class JabutiMetricCollector implements MetricCollector {

	private static final String COMMAND = "jabuti";

	private JabutiOutputParser outputParser;

	public JabutiMetricCollector() throws IOException {
		InputStream metricListOutput = new CommandTask(COMMAND + " --list").executeAndGetOuput();
		outputParser = new JabutiOutputParser(metricListOutput);
	}

	@Override
	public BaseTool getBaseTool() {
		BaseTool baseTool = new BaseTool("Jabuti");
		baseTool.setCollectorClass(JabutiMetricCollector.class);
		baseTool.setSupportedMetrics(outputParser.getSupportedMetrics());
		return baseTool;
	}

	@Override
	public Set<NativeModuleResult> collectMetrics(File codeDirectory, Set<NativeMetric> metrics) throws IOException {
		String command = COMMAND;
		InputStream jabutiOuput = new CommandTask(command, codeDirectory).executeAndGetOuput();
		return outputParser.parseResults(jabutiOuput, metrics);
	}
}