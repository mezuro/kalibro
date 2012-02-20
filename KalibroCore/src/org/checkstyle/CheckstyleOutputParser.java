package org.checkstyle;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.kalibro.core.model.Module;
import org.kalibro.core.model.NativeMetric;
import org.kalibro.core.model.NativeMetricResult;
import org.kalibro.core.model.NativeModuleResult;
import org.kalibro.core.model.enums.Granularity;

import com.puppycrawl.tools.checkstyle.api.AuditEvent;

public class CheckstyleOutputParser extends AuditAdapter {

	private Map<String, NativeModuleResult> resultsMap;
	private boolean finished;

	public CheckstyleOutputParser() {
		resultsMap = new HashMap<String, NativeModuleResult>();
		finished = true;
	}

	@Override
	public void addError(AuditEvent event) {
		String messageKey = event.getLocalizedMessage().getKey();
		String className = fileNameToClass(event.getFileName());
		Double value = Double.parseDouble(event.getMessage());
		addMetricResult(className, CheckstyleMetric.getNativeMetricFor(messageKey), value);
	}

	@Override
	public synchronized void auditStarted(AuditEvent aEvt) {
		resultsMap = new HashMap<String, NativeModuleResult>();
		finished = false;
	}

	@Override
	public synchronized void auditFinished(AuditEvent aEvt) {
		finished = true;
		notify();
	}

	public synchronized Set<NativeModuleResult> getResults() throws InterruptedException {
		while (! finished)
			wait();
		return new HashSet<NativeModuleResult>(resultsMap.values());
	}

	private String fileNameToClass(String group) {
		String[] parts = group.split("\\.|" + Pattern.quote(File.separator));
		return parts[parts.length - 2];
	}

	private void addMetricResult(String className, NativeMetric nativeMetric, Double value) {
		NativeMetricResult metricResult = new NativeMetricResult(nativeMetric, value);
		getModuleResult(className).addMetricResult(metricResult);
	}

	private NativeModuleResult getModuleResult(String className) {
		if (! resultsMap.containsKey(className))
			resultsMap.put(className, new NativeModuleResult(new Module(Granularity.CLASS, className)));
		return resultsMap.get(className);
	}
}