package org.checkstyle;

import com.puppycrawl.tools.checkstyle.api.AuditEvent;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.kalibro.core.model.Module;
import org.kalibro.core.model.NativeMetric;
import org.kalibro.core.model.NativeMetricResult;
import org.kalibro.core.model.NativeModuleResult;
import org.kalibro.core.model.enums.Granularity;

public class CheckstyleOutputParser extends AuditAdapter {

	private Map<String, NativeModuleResult> resultsMap;
	private boolean finished;

	public CheckstyleOutputParser() {
		resultsMap = new HashMap<String, NativeModuleResult>();
		finished = true;
	}

	@Override
	public void addError(AuditEvent event) {
		String className = fileNameToClass(event.getFileName());
		String message = event.getMessage();
		for (CheckstyleMetric metric : CheckstyleMetric.values()) {
			Matcher matcher = metric.getPattern().matcher(message);
			if (matcher.matches()) {
				Double value = new Double(matcher.group(1));
				addToNativeModuleResult(className, metric.getNativeMetric(), value);
			}
		}
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

	private void addToNativeModuleResult(String className, NativeMetric nativeMetric, Double value) {
		NativeModuleResult nativeModuleResult = getNativeModuleResult(className);
		nativeModuleResult.addMetricResult(new NativeMetricResult(nativeMetric, value));
	}

	private NativeModuleResult getNativeModuleResult(String className) {
		NativeModuleResult result;
		if (resultsMap.containsKey(className))
			result = resultsMap.get(className);
		else {
			result = new NativeModuleResult(new Module(Granularity.CLASS, className));
			resultsMap.put(className, result);
		}
		return result;
	}

	private String fileNameToClass(String group) {
		String[] parts = group.split("\\.|" + Pattern.quote(File.separator));
		return parts[parts.length - 2];
	}

}