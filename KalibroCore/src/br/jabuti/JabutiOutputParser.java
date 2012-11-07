package br.jabuti;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import org.apache.commons.io.IOUtils;
import org.kalibro.*;
import org.kalibro.core.concurrent.Writer;
import org.yaml.snakeyaml.Yaml;

public class JabutiOutputParser {

	private Map<String, NativeMetric> supportedMetrics;

	protected JabutiOutputParser(InputStream metricListOutput) throws IOException {
		parserSupportedMetrics(metricListOutput);
	}

	private void parserSupportedMetrics(InputStream metricListOutput) throws IOException {
		supportedMetrics = new LinkedHashMap<String, NativeMetric>();
		@SuppressWarnings("unchecked")
		List<String> lines = IOUtils.readLines(metricListOutput);
		for (String line : lines)
			if (line.contains(" - "))
				parseSupportedMetric(line);
	}

	public Collection<NativeMetric> getSupportedMetrics() {
		return supportedMetrics.values();
	}

	public void parseResults(InputStream jabutiOutput, Set<NativeMetric> metrics,
		Writer<NativeModuleResult> resultWriter) {
		for (Object resultMap : new Yaml().loadAll(jabutiOutput))
			resultWriter.write(parseResult((Map<?, ?>) resultMap, metrics));
		resultWriter.close();
	}

	private void parseSupportedMetric(String line) {
		int hyphenIndex = line.indexOf(" - ");
		String code = line.substring(0, hyphenIndex).trim();
		String name = line.substring(hyphenIndex + 3).trim();
		Granularity scope = Granularity.SOFTWARE;
		NativeMetric metric = new NativeMetric(name, scope, Language.JAVA);
		supportedMetrics.put(code, metric);
	}

	private NativeModuleResult parseResult(Map<?, ?> resultMap,
		Set<NativeMetric> wantedMetrics) {
		NativeModuleResult moduleResult = createModuleResult(resultMap);
		for (Object metricCode : resultMap.keySet()) {
			NativeMetric metric = supportedMetrics.get(metricCode);
			if (metric != null && wantedMetrics.contains(metric))
				addMetricResult(moduleResult, metric, resultMap.get(metricCode));
		}
		return moduleResult;
	}

	private NativeModuleResult createModuleResult(Map<?, ?> resultMap) {
		String moduleName = "" + resultMap.get("_module");
		Granularity granularity = moduleName.equals("null") ? Granularity.SOFTWARE : Granularity.CLASS;
		Module module = new Module(granularity, moduleName.split(":+"));
		return new NativeModuleResult(module);
	}

	private void addMetricResult(NativeModuleResult moduleResult, NativeMetric metric, Object result) {
		Double value = 0.0;
		if (null != result)
			value = Double.parseDouble("" + result);
		moduleResult.addMetricResult(new NativeMetricResult(metric, value));
	}
}