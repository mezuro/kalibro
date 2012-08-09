package br.jabuti;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.kalibro.KalibroException;
import org.kalibro.core.MetricCollector;
import org.kalibro.core.command.CommandTask;
import org.kalibro.core.command.FileProcessStreamLogger;
import org.kalibro.core.model.BaseTool;
import org.kalibro.core.model.NativeMetric;
import org.kalibro.core.model.NativeModuleResult;

public class JabutiMetricCollector implements MetricCollector {

	private BaseTool baseTool;
	private final String COMMAND = "jabuti";
	private final JabutiOutputParser outputParser = new JabutiOutputParser();
	
	@Override
	public BaseTool getBaseTool() {
		if (null == baseTool) {
			InputStream metricListOutput;
			try {
				metricListOutput = new CommandTask(COMMAND + " --list").executeAndGetOuput();
				this.baseTool = new BaseTool("Jabuti");
				this.baseTool.setCollectorClass(JabutiMetricCollector.class);
				outputParser.setSupportedMetrics(metricListOutput);
				this.baseTool.setSupportedMetrics(outputParser.getSupportedMetrics());
			} catch (IOException e) {
				throw new KalibroException(e.getMessage());
			}			
		}
		return baseTool;
	}

	@Override
	public Set<NativeModuleResult> collectMetrics(File codeDirectory,
			Set<NativeMetric> metrics) throws Exception {
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

}
