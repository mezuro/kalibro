package org.cvsanaly;

import org.cvsanaly.entities.MetricResult;
import org.kalibro.Granularity;
import org.kalibro.Language;
import org.kalibro.NativeMetric;
import org.kalibro.core.Identifier;
import org.kalibro.core.reflection.FieldReflector;

public enum CvsAnalyMetric {

	NUMBER_OF_SOURCE_LINES_OF_CODE,
	NUMBER_OF_LINES_OF_CODE,
	NUMBER_OF_COMMENTS,
	NUMBER_OF_COMMENTED_LINES,
	NUMBER_OF_BLANK_LINES,
	NUMBER_OF_FUNCTIONS,
	MAXIMUM_CYCLOMATIC_COMPLEXITY,
	AVERAGE_CYCLOMATIC_COMPLEXITY,
	HALSTEAD_VOLUME;

	@Override
	public String toString() {
		return Identifier.fromConstant(name()).asText();
	}

	public double getMetricValue(MetricResult metricResult) {
		FieldReflector fieldReflector = new FieldReflector(metricResult);
		String fieldName = Identifier.fromConstant(name()).asVariable();
		return (Double) fieldReflector.get(fieldName);
	}

	protected NativeMetric getNativeMetric() {
		NativeMetric nativeMetric = new NativeMetric(toString(), Granularity.CLASS,
			Language.C, Language.CPP, Language.JAVA, Language.PYTHON);
		return nativeMetric;
	}
}