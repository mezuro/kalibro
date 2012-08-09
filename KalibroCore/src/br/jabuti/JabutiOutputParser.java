package br.jabuti;

import static org.kalibro.core.model.enums.Granularity.APPLICATION;
import static org.kalibro.core.model.enums.Granularity.CLASS;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.kalibro.core.model.Module;
import org.kalibro.core.model.NativeMetric;
import org.kalibro.core.model.NativeMetricResult;
import org.kalibro.core.model.NativeModuleResult;
import org.kalibro.core.model.enums.Granularity;
import org.kalibro.core.model.enums.Language;
import org.yaml.snakeyaml.Yaml;

public class JabutiOutputParser {

	private Map<String, NativeMetric> supportedMetrics;

	public void setSupportedMetrics(InputStream metricListOutput) throws IOException {
		supportedMetrics = new LinkedHashMap<String, NativeMetric>(); 
		List<String> lines = IOUtils.readLines(metricListOutput);
		for (String line : lines)
			if (line.contains(" - "))
				parseSupportedMetric(line);		
	}
	
	public Collection<NativeMetric> getSupportedMetrics() {
		return supportedMetrics.values();
	}

	public Set<NativeModuleResult> parseResults(InputStream jabutiOutput,
			Set<NativeMetric> metrics) {
		Set<NativeModuleResult> results = new LinkedHashSet<NativeModuleResult>();
		for (Object resultMap : new Yaml().loadAll(jabutiOutput))
			results.add(parseResult((Map<?, ?>) resultMap, metrics));	
		return results;
	}
	
	private void parseSupportedMetric(String line) {
		int hyphenIndex = line.indexOf(" - ");
		String code = line.substring(0, hyphenIndex).trim();
		String name = line.substring(hyphenIndex + 3).trim();
		Granularity scope = APPLICATION;
		NativeMetric metric = new NativeMetric(name, scope, Language.JAVA);
		metric.setOrigin("Jabuti");
		supportedMetrics.put(code, metric);
	}
	
	private NativeModuleResult parseResult(Map<?, ?> resultMap, Set<NativeMetric> wantedMetrics) {
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
		Granularity granularity = moduleName.equals("null") ? APPLICATION : CLASS;
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
