package org.kalibro.core.model;

import static org.kalibro.core.model.MetricConfigurationFixtures.*;

import org.kalibro.Configuration;

public final class ConfigurationFixtures {

	public static final String CONFIGURATION_NAME = "Kalibro for Java";

	private static Configuration kalibroConfiguration = newKalibroConfiguration();

	public static Configuration kalibroConfiguration() {
		return kalibroConfiguration;
	}

	public static Configuration newKalibroConfiguration() {
		return newConfiguration(configurationCodes().toArray(new String[0]));
	}

	public static Configuration newConfiguration(String... codes) {
		Configuration configuration = new Configuration();
		configuration.setName(CONFIGURATION_NAME);
		configuration.setDescription("Kalibro configuration for Java projects. " +
			"Thresholds obtained from literature and statistic experiments with Analizo.");
		for (String code : codes)
			configuration.addMetricConfiguration(newMetricConfiguration(code));
		return configuration;
	}

	private ConfigurationFixtures() {
		return;
	}
}