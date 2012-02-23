package org.kalibro.core.model;

import static org.kalibro.core.model.MetricConfigurationFixtures.*;

public final class ConfigurationFixtures {

	public static Configuration kalibroConfiguration() {
		Configuration configuration = new Configuration();
		configuration.setName("Kalibro for Java");
		configuration.setDescription("Kalibro configuration for Java projects. " +
			"Thresholds obtained from literature and statistic experiments with Analizo.");
		for (String code : metricsWithThresholds())
			configuration.addMetricConfiguration(configuration(code));
		return configuration;
	}

	private ConfigurationFixtures() {
		// Utility class
	}
}