package br.jabuti;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.kalibro.KalibroException;
import org.kalibro.MetricCollector;
import org.kalibro.NativeMetric;
import org.kalibro.NativeModuleResult;
import org.kalibro.core.command.CommandTask;
import org.kalibro.core.concurrent.Writer;

public class JabutiMetricCollector implements MetricCollector {

	private static final String COMMAND = "jabuti";

	private JabutiOutputParser outputParser;
	private InputStream metricListOutput;

	public JabutiMetricCollector() {
		try {
			metricListOutput = new CommandTask(COMMAND + " --list").executeAndGetOuput();
			outputParser = new JabutiOutputParser(metricListOutput);
		} catch (IOException e) {
			throw new KalibroException(e.getMessage());
		}
	}

	@Override
	public String name() {
		return "Jabuti";
	}

	@Override
	public String description() {
		return "";
	}

	@Override
	public Set<NativeMetric> supportedMetrics() {
		return new HashSet<NativeMetric>(outputParser.getSupportedMetrics());
	}

	@Override
	public void collectMetrics(
		File codeDirectory, Set<NativeMetric> wantedMetrics, Writer<NativeModuleResult> resultWriter) throws Exception {
		InputStream jabutiOutput = new CommandTask(COMMAND, findWorkDirectory(codeDirectory)).executeAndGetOuput();
		outputParser.parseResults(jabutiOutput, wantedMetrics, resultWriter);
	}

	private File findWorkDirectory(File codeDirectory) throws IOException {
		String[] cmdFind = {"/bin/sh",
			"-c",
			"find . -maxdepth 2 -name \"jabuti.conf\" | head -1"};

		Process find = Runtime.getRuntime().exec(cmdFind, null, codeDirectory);
		if ("".equals(IOUtils.toString(find.getInputStream()).replace("\n", "")))
			throw new KalibroException("File jabuti.conf not found");

		String[] cmdFindDirectory = {"/bin/sh",
			"-c",
			"dirname \"$(pwd .)/$(find . -maxdepth 2 -name \"jabuti.conf\" | head -1)\""};

		Process process = Runtime.getRuntime().exec(cmdFindDirectory, null, codeDirectory);

		return new File(IOUtils.toString(process.getInputStream()).replace("\n", "")).getParentFile();
	}

}