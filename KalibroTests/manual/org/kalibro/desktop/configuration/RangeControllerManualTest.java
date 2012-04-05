package org.kalibro.desktop.configuration;

import static org.kalibro.core.model.MetricConfigurationFixtures.*;

import org.kalibro.core.model.MetricConfiguration;

public final class RangeControllerManualTest {

	public static void main(String[] args) {
		MetricConfiguration configuration = metricConfiguration("amloc");
		new RangeController(configuration).editRange(configuration.getRangeFor(0.0));
		System.out.println("Ranges: " + configuration.getRanges());
	}

	private RangeControllerManualTest() {
		return;
	}
}