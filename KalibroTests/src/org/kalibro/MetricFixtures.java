package org.kalibro;

import static org.kalibro.Granularity.*;
import static org.kalibro.Language.*;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.yaml.snakeyaml.Yaml;

public final class MetricFixtures {

	private static CompoundMetric sc = newSc();
	private static Map<String, String> analizoMetricNames;
	private static Map<String, NativeMetric> analizoMetricsMap;

	static {
		InputStream resource = MetricFixtures.class.getResourceAsStream("analizo_metrics.yml");
		analizoMetricNames = new Yaml().loadAs(resource, Map.class);
		analizoMetricsMap = new HashMap<String, NativeMetric>();
		for (String code : analizoMetricNames.keySet())
			analizoMetricsMap.put(code, newAnalizoMetric(code));
	}

	public static Set<String> analizoMetricCodes() {
		return analizoMetricsMap.keySet();
	}

	public static NativeMetric analizoMetric(String code) {
		return analizoMetricsMap.get(code);
	}

	public static NativeMetric newAnalizoMetric(String code) {
		Granularity scope = code.startsWith("total") ? SOFTWARE : CLASS;
		NativeMetric metric = new NativeMetric(analizoMetricNames.get(code), scope, C, CPP, JAVA);
		metric.setOrigin("Analizo");
		return metric;
	}

	public static CompoundMetric sc() {
		return sc;
	}

	public static CompoundMetric newSc() {
		CompoundMetric metric = new CompoundMetric();
		metric.setName("Structural complexity");
		metric.setScript("return cbo * lcom4;");
		return metric;
	}

	private MetricFixtures() {
		return;
	}
}