package org.cvsanaly;

import static org.kalibro.Language.*;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import org.kalibro.Granularity;
import org.kalibro.NativeMetric;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.introspector.BeanAccess;

/**
 * A metric collected by CVSAnalY. Extends {@link NativeMetric} adding query column to retrieve metric values.
 * 
 * @author Carlos Morais
 * @author Eduardo Morais
 */
final class CvsAnalyMetric extends NativeMetric {

	private static Set<CvsAnalyMetric> metrics;

	static {
		initializeMetrics();
	}

	static Set<NativeMetric> supportedMetrics() {
		return new HashSet<NativeMetric>(metrics);
	}

	static Set<CvsAnalyMetric> selectMetrics(Set<NativeMetric> wantedMetrics) {
		Set<CvsAnalyMetric> selectedMetrics = new HashSet<CvsAnalyMetric>(metrics);
		selectedMetrics.retainAll(wantedMetrics);
		return selectedMetrics;
	}

	private static void initializeMetrics() {
		metrics = new HashSet<CvsAnalyMetric>();
		Yaml yaml = new Yaml();
		yaml.setBeanAccess(BeanAccess.FIELD);
		InputStream metricsStream = CvsAnalyMetric.class.getResourceAsStream("supported-metrics.yml");
		for (Object object : yaml.loadAll(metricsStream))
			metrics.add((CvsAnalyMetric) object);
	}

	private String column;

	private CvsAnalyMetric() {
		super("", Granularity.CLASS, C, CPP, JAVA, PYTHON);
	}

	String getColumn() {
		return column;
	}
}