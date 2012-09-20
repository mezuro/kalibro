package br.jabuti;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

<<<<<<< HEAD
import org.apache.commons.io.IOUtils;
import org.kalibro.KalibroException;
import org.kalibro.core.MetricCollector;
import org.kalibro.core.command.CommandTask;
import org.kalibro.core.command.FileProcessStreamLogger;
=======
import br.jabuti.JabutiOutputParser;
import org.kalibro.core.MetricCollector;
import org.kalibro.core.command.CommandTask;
>>>>>>> d8862dc7cc2046762e2eebd386a46702a2d5681f
import org.kalibro.core.model.BaseTool;
import org.kalibro.core.model.NativeMetric;
import org.kalibro.core.model.NativeModuleResult;

<<<<<<< HEAD
/*
 * Jabuti Metric Collector
 */
public class JabutiMetricCollector implements MetricCollector {

	private BaseTool baseTool;
	private final String COMMAND = "jabuti";
	private JabutiOutputParser outputParser;
	private InputStream metricListOutput;
	
	public JabutiMetricCollector() {
		try {
			metricListOutput = new CommandTask(COMMAND + " --list").executeAndGetOuput();
			outputParser = new JabutiOutputParser(metricListOutput);
		} catch (IOException e) {
			throw new KalibroException(e.getMessage());
		}			
=======
public class JabutiMetricCollector implements MetricCollector {

	private static final String COMMAND = "jabuti";

	private JabutiOutputParser outputParser;

	public JabutiMetricCollector() throws IOException {
		InputStream metricListOutput = new CommandTask(COMMAND + " --list").executeAndGetOuput();
		outputParser = new JabutiOutputParser(metricListOutput);
>>>>>>> d8862dc7cc2046762e2eebd386a46702a2d5681f
	}

	@Override
	public BaseTool getBaseTool() {
<<<<<<< HEAD
		if (null == baseTool) {			
			this.baseTool = new BaseTool("Jabuti");
			this.baseTool.setCollectorClass(JabutiMetricCollector.class);
			this.baseTool.setSupportedMetrics(outputParser.getSupportedMetrics());
		}
=======
		BaseTool baseTool = new BaseTool("Jabuti");
		baseTool.setCollectorClass(JabutiMetricCollector.class);
		baseTool.setSupportedMetrics(outputParser.getSupportedMetrics());
>>>>>>> d8862dc7cc2046762e2eebd386a46702a2d5681f
		return baseTool;
	}

	@Override
<<<<<<< HEAD
	public Set<NativeModuleResult> collectMetrics(File codeDirectory, Set<NativeMetric> metrics) throws Exception {
		InputStream jabutiOuput = new CommandTask(COMMAND, this.findWorkDirectory(codeDirectory)).executeAndGetOuput();
		return outputParser.parseResults(jabutiOuput, metrics);
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
		new FileProcessStreamLogger().logErrorStream(process, cmdFindDirectory.toString());

		return new File(IOUtils.toString(process.getInputStream()).replace("\n", "")).getParentFile();
	}

=======
	public Set<NativeModuleResult> collectMetrics(File codeDirectory, Set<NativeMetric> metrics) throws IOException {
		String command = COMMAND;
		InputStream jabutiOuput = new CommandTask(command, codeDirectory).executeAndGetOuput();
		return outputParser.parseResults(jabutiOuput, metrics);
	}
>>>>>>> d8862dc7cc2046762e2eebd386a46702a2d5681f
}