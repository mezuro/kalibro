package org.analizo;

import static org.kalibro.core.model.enums.Granularity.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import org.apache.commons.io.IOUtils;
import org.kalibro.core.model.Module;
import org.kalibro.core.model.NativeMetric;
import org.kalibro.core.model.NativeMetricResult;
import org.kalibro.core.model.NativeModuleResult;
import org.kalibro.core.model.enums.Granularity;
import org.kalibro.core.model.enums.Language;
import org.yaml.snakeyaml.Yaml;

public class AnalizoOutputParser {

	private Map<String, NativeMetric> supportedMetrics;

	public AnalizoOutputParser(InputStream metricListOuput) throws IOException {
		supportedMetrics = new HashMap<String, NativeMetric>();
		parseSupportedMetrics(metricListOuput);
	}

	private void parseSupportedMetrics(InputStream metricListOutput) throws IOException {
		List<String> lines = IOUtils.readLines(metricListOutput);
		for (String line : lines)
			if (line.contains(" - "))
				parseSupportedMetric(line);
	}

	private void parseSupportedMetric(String line) {
		int hyphenIndex = line.indexOf('-');
		String code = line.substring(0, hyphenIndex).trim();
		String name = line.substring(hyphenIndex + 1).trim();
		Granularity scope = code.startsWith("total") ? APPLICATION : CLASS;
		NativeMetric metric = new NativeMetric(name, scope, Language.values());
		supportedMetrics.put(code, metric);
	}

	public Set<NativeMetric> getSupportedMetrics() {
		return new HashSet<NativeMetric>(supportedMetrics.values());
	}

	public Set<NativeModuleResult> parseResults(InputStream resultsOutput, Set<NativeMetric> wantedMetrics) {
		Set<NativeModuleResult> results = new HashSet<NativeModuleResult>();
		for (Object resultMap : new Yaml().loadAll(resultsOutput))
			results.add(parseResult((Map<?, ?>) resultMap, wantedMetrics));
		return results;
	}

	private NativeModuleResult parseResult(Map<?, ?> resultMap, Set<NativeMetric> wantedMetrics) {
		NativeModuleResult moduleResult = createModuleResult(resultMap);
		for (Object metricCode : resultMap.keySet()) {
			NativeMetric metric = supportedMetrics.get(metricCode);
			if (wantedMetrics.contains(metric))
				addMetricResult(moduleResult, metric, resultMap.get(metricCode));
		}
		return moduleResult;
	}

	private NativeModuleResult createModuleResult(Map<?, ?> resultMap) {
		String moduleName = "" + resultMap.get("_module");
		Granularity granularity = moduleName.equals("null") ? APPLICATION : CLASS;
		Module module = new Module(granularity, moduleName.split(":+"));
		return new NativeModuleResult(module);
	}

	private void addMetricResult(NativeModuleResult moduleResult, NativeMetric metric, Object result) {
		Double value = Double.parseDouble("" + result);
		moduleResult.addMetricResult(new NativeMetricResult(metric, value));
	}
}