package org.kalibro.desktop.configuration;

import org.kalibro.core.model.MetricConfiguration;
import org.kalibro.core.model.MetricConfigurationFixtures;

public class RangeDialogManualTest {

	public static void main(String[] args) {
		MetricConfiguration configuration = MetricConfigurationFixtures.configuration("amloc");
		new RangeController(configuration).editRange(configuration.getRangeFor(0.0));
		System.out.println("Ranges: " + configuration.getRanges());
	}
}