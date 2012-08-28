package org.checkstyle;

import com.puppycrawl.tools.checkstyle.api.AuditEvent;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.kalibro.core.model.Module;
import org.kalibro.core.model.NativeMetric;
import org.kalibro.core.model.NativeModuleResult;
import org.kalibro.core.model.enums.Granularity;

public class CheckstyleOutputParser extends AuditAdapter {

	private File codeDirectory;
	private Set<NativeMetric> wantedMetrics;
	private Map<Module, PreModuleResult> resultsMap;

	public CheckstyleOutputParser(File codeDirectory, Set<NativeMetric> wantedMetrics) {
		this.codeDirectory = codeDirectory;
		this.wantedMetrics = wantedMetrics;
		resultsMap = new HashMap<Module, PreModuleResult>();
	}

	@Override
	public void auditStarted(AuditEvent aEvt) {
		resultsMap = new HashMap<Module, PreModuleResult>();
	}

	@Override
	public void addError(AuditEvent event) {
		String messageKey = event.getLocalizedMessage().getKey();
		Module module = fileNameToModule(event.getFileName());
		String message = event.getMessage().replace(messageKey, "");
		Double value = parseValue(message);
		addMetricResult(module, messageKey, value);
	}

	private Module fileNameToModule(String fileName) {
		String relativeFileName = fileName.replace(codeDirectory.getAbsolutePath() + File.separator, "");
		String[] parts = relativeFileName.replace(".java", "").split(Pattern.quote(File.separator));
		return new Module(Granularity.CLASS, parts);
	}

	private Double parseValue(String message) {
		try {
			return Double.parseDouble(message);
		} catch (NumberFormatException exception) {
			return 0.0;
		}
	}

	private void addMetricResult(Module module, String messageKey, Double value) {
		getPreResult(module).addMetricResult(messageKey, value);
	}

	private PreModuleResult getPreResult(Module module) {
		if (!resultsMap.containsKey(module))
			resultsMap.put(module, new PreModuleResult(module, wantedMetrics));
		return resultsMap.get(module);
	}

	public Set<NativeModuleResult> getResults() {
		Set<NativeModuleResult> results = new HashSet<NativeModuleResult>();
		for (PreModuleResult result : resultsMap.values())
			results.add(result.getModuleResult());
		return results;
	}
}