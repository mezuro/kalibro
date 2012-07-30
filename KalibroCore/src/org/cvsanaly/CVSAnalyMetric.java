package org.cvsanaly;

import java.lang.reflect.Method;

import org.cvsanaly.entities.MetricResult;
import org.kalibro.core.model.NativeMetric;
import org.kalibro.core.model.enums.Granularity;
import org.kalibro.core.model.enums.Language;
import org.kalibro.core.util.Identifier;

public enum CVSAnalyMetric {
	// TODO Add more metrics
	NUMBER_OF_SOURCE_LINES_OF_CODE("getNumberOfSourceCodeLines"),
	NUMBER_OF_LINES_OF_CODE("getNumberOfLinesOfCode"),
	NUMBER_OF_COMMENTS("getNumberOfComments"),
	NUMBER_OF_COMMENTED_LINES("getNumberOfCommentedLines"),
	NUMBER_OF_BLANK_LINES("getNumberOfBlankLines"),
	NUMBER_OF_FUNCTIONS("getNumberOfFunctions");

	private Method methodToGetMetricValue;
	
	private CVSAnalyMetric(String methodName) {
			methodToGetMetricValue = getMethodFromMethodName(methodName);
	}

	private Method getMethodFromMethodName(String methodName) {
		try {
			return MetricResult.class.getMethod(methodName);
		} catch (Exception e) {
			throw new ExceptionInInitializerError("Couldn't get method");
		}
	}
	
	@Override
	public String toString() {
		return Identifier.fromConstant(name()).asText();
	}
	
	public double getMetricValue(MetricResult metricResult) throws ReflectiveOperationException {
		return (Double) methodToGetMetricValue.invoke(metricResult);
	}

	protected NativeMetric getNativeMetric() {
		NativeMetric nativeMetric = new NativeMetric(toString(), Granularity.CLASS, Language.values());
		nativeMetric.setOrigin("CVSAnaly");
		return nativeMetric;
	}
}