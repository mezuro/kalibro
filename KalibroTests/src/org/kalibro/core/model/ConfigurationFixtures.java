package org.kalibro.core.model;

import static org.kalibro.core.model.MetricConfigurationFixtures.*;

import java.util.Arrays;
import java.util.Collection;

public final class ConfigurationFixtures {

	public static Configuration kalibroConfiguration() {
		return createConfiguration(metricsWithThresholds());
	}

	public static Configuration simpleConfiguration() {
		return createConfiguration(Arrays.asList("amloc", "cbo", "lcom4"));
	}

	private static Configuration createConfiguration(Collection<String> codes) {
		Configuration configuration = new Configuration();
		configuration.setName("Kalibro for Java");
		configuration.setDescription("Kalibro configuration for Java projects. " +
			"Thresholds obtained from literature and statistic experiments with Analizo.");
		for (String code : codes)
			configuration.addMetricConfiguration(configuration(code));
		return configuration;
	}

	private ConfigurationFixtures() {
		return;
	}
}