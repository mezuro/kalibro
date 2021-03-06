package org.analizo;

import static org.kalibro.Granularity.*;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.kalibro.*;
import org.kalibro.core.concurrent.Writer;
import org.yaml.snakeyaml.Yaml;

/**
 * Parser for Analizo results output.
 * 
 * @author Carlos Morais
 */
class AnalizoResultParser {

	private Map<String, NativeMetric> wantedMetrics;

	AnalizoResultParser(Map<NativeMetric, String> supportedMetrics, Set<NativeMetric> wantedMetrics) {
		this.wantedMetrics = new HashMap<String, NativeMetric>();
		for (NativeMetric metric : supportedMetrics.keySet())
			if (wantedMetrics.contains(metric))
				this.wantedMetrics.put(supportedMetrics.get(metric), metric);
	}

	void parse(InputStream input, Writer<NativeModuleResult> resultWriter) {
		for (Object resultMap : new Yaml().loadAll(input))
			resultWriter.write(parseResult((Map<?, ?>) resultMap));
	}

	private NativeModuleResult parseResult(Map<?, ?> resultMap) {
		NativeModuleResult moduleResult = createModuleResult(resultMap);
		for (Object code : resultMap.keySet())
			if (wantedMetrics.containsKey(code))
				moduleResult.addMetricResult(metricResult(code, resultMap.get(code)));
		return moduleResult;
	}

	private NativeModuleResult createModuleResult(Map<?, ?> resultMap) {
		String moduleName = "" + resultMap.get("_module");
		Granularity granularity = moduleName.equals("null") ? SOFTWARE : CLASS;
		Module module = new Module(granularity, moduleName.split(":+"));
		return new NativeModuleResult(module);
	}

	private NativeMetricResult metricResult(Object code, Object result) {
		Double value = Double.parseDouble("" + result);
		return new NativeMetricResult(wantedMetrics.get(code), value);
	}
}