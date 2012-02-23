package org.checkstyle;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.kalibro.core.model.NativeMetric;
import org.kalibro.core.model.enums.Granularity;
import org.kalibro.core.model.enums.Language;
import org.kalibro.core.util.Identifier;

public enum CheckstyleMetric {

	FILE_LENGTH("FileLength", "max", "maxLen.file"),
	NUMBER_OF_METHODS("TreeWalker.MethodCount", "maxTotal", "too.many.methods");

	private static Map<String, NativeMetric> supportedMetrics;

	public static Set<NativeMetric> supportedMetrics() {
		return new HashSet<NativeMetric>(supportedMetrics.values());
	}

	public static NativeMetric getNativeMetricFor(String messageKey) {
		return supportedMetrics.get(messageKey);
	}

	private static void addSupportedMetric(String messageKey, String metricName) {
		if (supportedMetrics == null)
			supportedMetrics = new HashMap<String, NativeMetric>();
		supportedMetrics.put(messageKey, new NativeMetric(metricName, Granularity.CLASS, Language.JAVA));
	}

	private String[] modulePath;
	private String attributeName;
	private String messageKey;

	private CheckstyleMetric(String modulePath, String attributeName, String messageKey) {
		this.modulePath = modulePath.split("\\.");
		this.attributeName = attributeName;
		this.messageKey = messageKey;
		addSupportedMetric(messageKey, toString());
	}

	@Override
	public String toString() {
		return Identifier.fromConstant(name()).asText();
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