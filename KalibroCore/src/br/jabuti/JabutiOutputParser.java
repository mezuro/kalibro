package br.jabuti;

<<<<<<< HEAD
=======
import static org.kalibro.core.model.enums.Granularity.APPLICATION;
>>>>>>> d8862dc7cc2046762e2eebd386a46702a2d5681f
import static org.kalibro.core.model.enums.Granularity.CLASS;

import java.io.IOException;
import java.io.InputStream;
<<<<<<< HEAD
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
=======
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
>>>>>>> d8862dc7cc2046762e2eebd386a46702a2d5681f

import org.apache.commons.io.IOUtils;
import org.kalibro.core.model.Module;
import org.kalibro.core.model.NativeMetric;
import org.kalibro.core.model.NativeMetricResult;
import org.kalibro.core.model.NativeModuleResult;
import org.kalibro.core.model.enums.Granularity;
import org.kalibro.core.model.enums.Language;
import org.yaml.snakeyaml.Yaml;

<<<<<<< HEAD
/**
 * Jabuti Output Parser
 *
 */
=======
>>>>>>> d8862dc7cc2046762e2eebd386a46702a2d5681f
public class JabutiOutputParser {

	private Map<String, NativeMetric> supportedMetrics;

<<<<<<< HEAD
	protected JabutiOutputParser(InputStream metricListOutput) throws IOException {
		parserSupportedMetrics(metricListOutput);
	}

	private void parserSupportedMetrics(InputStream metricListOutput) throws IOException {
		supportedMetrics = new LinkedHashMap<String, NativeMetric>();
=======
	protected JabutiOutputParser(InputStream metricListOuput) throws IOException {
		supportedMetrics = new HashMap<String, NativeMetric>();
		parseSupportedMetrics(metricListOuput);
	}

	private void parseSupportedMetrics(InputStream metricListOutput) throws IOException {
>>>>>>> d8862dc7cc2046762e2eebd386a46702a2d5681f
		@SuppressWarnings("unchecked")
		List<String> lines = IOUtils.readLines(metricListOutput);
		for (String line : lines)
			if (line.contains(" - "))
				parseSupportedMetric(line);
	}

<<<<<<< HEAD
	public Collection<NativeMetric> getSupportedMetrics() {
		return supportedMetrics.values();
	}

	public Set<NativeModuleResult> parseResults(InputStream jabutiOutput, Set<NativeMetric> metrics) {
		Set<NativeModuleResult> results = new LinkedHashSet<NativeModuleResult>();
		for (Object resultMap : new Yaml().loadAll(jabutiOutput))
			results.add(parseResult((Map<?, ?>) resultMap, metrics));
		return results;
	}

=======
>>>>>>> d8862dc7cc2046762e2eebd386a46702a2d5681f
	private void parseSupportedMetric(String line) {
		int hyphenIndex = line.indexOf(" - ");
		String code = line.substring(0, hyphenIndex).trim();
		String name = line.substring(hyphenIndex + 3).trim();
<<<<<<< HEAD
		Granularity scope = Granularity.SOFTWARE;
		NativeMetric metric = new NativeMetric(name, scope, Language.JAVA);
=======
		Granularity scope = code.startsWith("total") ? APPLICATION : CLASS;
		NativeMetric metric = new NativeMetric(name, scope, Language.values());
>>>>>>> d8862dc7cc2046762e2eebd386a46702a2d5681f
		metric.setOrigin("Jabuti");
		supportedMetrics.put(code, metric);
	}

<<<<<<< HEAD
	private NativeModuleResult parseResult(Map<?, ?> resultMap,
			Set<NativeMetric> wantedMetrics) {
=======
	protected Set<NativeMetric> getSupportedMetrics() {
		return new TreeSet<NativeMetric>(supportedMetrics.values());
	}

	protected Set<NativeModuleResult> parseResults(InputStream resultsOutput, Set<NativeMetric> wantedMetrics) {
		Set<NativeModuleResult> results = new HashSet<NativeModuleResult>();
		for (Object resultMap : new Yaml().loadAll(resultsOutput))
			results.add(parseResult((Map<?, ?>) resultMap, wantedMetrics));
		return results;
	}

	private NativeModuleResult parseResult(Map<?, ?> resultMap, Set<NativeMetric> wantedMetrics) {
>>>>>>> d8862dc7cc2046762e2eebd386a46702a2d5681f
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
<<<<<<< HEAD
		Granularity granularity = moduleName.equals("null") ? Granularity.SOFTWARE : CLASS;
=======
		Granularity granularity = moduleName.equals("null") ? APPLICATION : CLASS;
>>>>>>> d8862dc7cc2046762e2eebd386a46702a2d5681f
		Module module = new Module(granularity, moduleName.split(":+"));
		return new NativeModuleResult(module);
	}

	private void addMetricResult(NativeModuleResult moduleResult, NativeMetric metric, Object result) {
<<<<<<< HEAD
		Double value = 0.0;
		if (null != result)
			value = Double.parseDouble("" + result);
		moduleResult.addMetricResult(new NativeMetricResult(metric, value));
	}

=======
		Double value = Double.parseDouble("" + result);
		moduleResult.addMetricResult(new NativeMetricResult(metric, value));
	}
>>>>>>> d8862dc7cc2046762e2eebd386a46702a2d5681f
}