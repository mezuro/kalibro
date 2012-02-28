package org.checkstyle;

import java.util.HashMap;
import java.util.Map;

import org.kalibro.core.model.NativeMetric;
import org.kalibro.core.model.enums.Granularity;
import org.kalibro.core.model.enums.Language;
import org.kalibro.core.model.enums.Statistic;
import org.kalibro.core.util.Identifier;

public enum CheckstyleMetric {

	AVERAGE_ANONYMOUS_CLASSES_LENGTH("TreeWalker.AnonInnerLength", "max", "maxLen.anonInner"),
	AVERAGE_METHOD_LENGTH("TreeWalker.MethodLength", "max", "maxLen.method"),
	EXECUTABLE_STATEMENTS("TreeWalker.ExecutableStatementCount", "max", "executableStatementCount", Statistic.SUM),
	FILE_LENGTH("FileLength", "max", "maxLen.file"),
	NUMBER_OF_METHODS("TreeWalker.MethodCount", "maxTotal", "too.many.methods");

	private static Map<String, CheckstyleMetric> metrics;

	public static CheckstyleMetric getMetricFor(String messageKey) {
		return metrics.get(messageKey);
	}

	private static void addMetric(String messageKey, CheckstyleMetric metric) {
		if (metrics == null)
			metrics = new HashMap<String, CheckstyleMetric>();
		metrics.put(messageKey, metric);
	}

	private String[] modulePath;
	private String attributeName;
	private String messageKey;
	private NativeMetric nativeMetric;
	private Statistic aggregationType;

	private CheckstyleMetric(String modulePath, String attributeName, String messageKey) {
		this(modulePath, attributeName, messageKey, Statistic.AVERAGE);
	}

	private CheckstyleMetric(String modulePath, String attributeName, String messageKey, Statistic aggregationType) {
		this.modulePath = modulePath.split("\\.");
		this.attributeName = attributeName;
		this.messageKey = messageKey;
		this.nativeMetric = new NativeMetric(toString(), Granularity.CLASS, Language.JAVA);
		this.aggregationType = aggregationType;
		addMetric(messageKey, this);
	}

	@Override
	public String toString() {
		return Identifier.fromConstant(name()).asText();
	}

	public String getMessageKey() {
		return messageKey;
	}

	public NativeMetric getNativeMetric() {
		return nativeMetric;
	}

	public Statistic getAggregationType() {
		return aggregationType;
	}

	public void addToChecker(CheckstyleConfiguration checker) {
		addTo(checker, 0);
	}

	private void addTo(CheckstyleConfiguration configuration, int firstIndex) {
		if (firstIndex == modulePath.length)
			addTo(configuration);
		else
			addTo(configuration.getChildByName(modulePath[firstIndex]), firstIndex + 1);
	}

	private void addTo(CheckstyleConfiguration configuration) {
		configuration.addAttributeName(attributeName);
		configuration.addMessageKey(messageKey);
	}
}