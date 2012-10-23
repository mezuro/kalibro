package org.checkstyle;

import com.puppycrawl.tools.checkstyle.api.AuditEvent;
import com.puppycrawl.tools.checkstyle.api.AuditListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.kalibro.*;
import org.kalibro.core.Environment;
import org.kalibro.core.concurrent.Writer;

/**
 * Listens to Checkstyle events and writes results according to them.
 * 
 * @author Carlos Morais
 * @author Eduardo Morais
 */
class CheckstyleListener implements AuditListener {

	private String codePath;
	private Set<CheckstyleMetric> wantedMetrics;
	private Writer<NativeModuleResult> resultWriter;

	private Module module;
	private Map<String, List<Double>> resultsMap;

	private PrintStream logStream;

	CheckstyleListener(File codeDirectory, Set<NativeMetric> wantedMetrics, Writer<NativeModuleResult> resultWriter) {
		this.codePath = codeDirectory.getAbsolutePath() + File.separator;
		this.wantedMetrics = CheckstyleMetric.selectMetrics(wantedMetrics);
		this.resultWriter = resultWriter;
	}

	@Override
	public void auditStarted(AuditEvent event) {
		resultsMap = new HashMap<String, List<Double>>();
	}

	@Override
	public void fileStarted(AuditEvent event) {
		String relativeFileName = event.getFileName().replace(codePath, "");
		String[] nameParts = relativeFileName.split(Pattern.quote(File.separator));
		module = new Module(Granularity.CLASS, nameParts);
		for (CheckstyleMetric wantedMetric : wantedMetrics)
			resultsMap.put(wantedMetric.getMessageKey(), new ArrayList<Double>());
	}

	@Override
	public void addError(AuditEvent event) {
		String messageKey = event.getLocalizedMessage().getKey();
		String value = event.getMessage().replace(messageKey, "");
		resultsMap.get(messageKey).add(parse(value));
	}

	private Double parse(String value) {
		try {
			return Double.parseDouble(value);
		} catch (NumberFormatException exception) {
			return 0.0;
		}
	}

	@Override
	public void fileFinished(AuditEvent event) {
		NativeModuleResult moduleResult = new NativeModuleResult(module);
		for (CheckstyleMetric metric : wantedMetrics)
			moduleResult.addMetricResult(metricResultFor(metric));
		resultWriter.write(moduleResult);
	}

	private NativeMetricResult metricResultFor(CheckstyleMetric metric) {
		List<Double> values = resultsMap.get(metric.getMessageKey());
		Double value = values.isEmpty() ? 0.0 : metric.getAggregationType().calculate(values);
		return new NativeMetricResult(metric, value);
	}

	@Override
	public void auditFinished(AuditEvent event) {
		resultWriter.close();
	}

	@Override
	public void addException(AuditEvent event, Throwable throwable) {
		try {
			logError(event.getFileName(), throwable);
		} catch (IOException exception) {
			return;
		}
	}

	private void logError(String fileName, Throwable throwable) throws IOException {
		if (logStream == null)
			logStream = new PrintStream(new FileOutputStream(createFile(), true));
		IOUtils.write("\n\nError auditing " + fileName + "\n", logStream);
		throwable.printStackTrace(logStream);
	}

	private File createFile() {
		String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		return new File(Environment.logsDirectory(), "checkstyle." + today + ".log");
	}
}