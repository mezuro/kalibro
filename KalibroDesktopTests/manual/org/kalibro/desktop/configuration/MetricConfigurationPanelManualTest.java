package org.kalibro.desktop.configuration;

import static org.kalibro.tests.UnitTest.loadFixture;

import org.kalibro.CompoundMetric;
import org.kalibro.MetricConfiguration;
import org.kalibro.desktop.tests.ComponentWrapperDialog;

@Deprecated
public final class MetricConfigurationPanelManualTest {

	public static void main(String[] args) {
		new MetricConfigurationPanelManualTest(loadFixture("lcom4", MetricConfiguration.class));
		new MetricConfigurationPanelManualTest(new MetricConfiguration(loadFixture("sc", CompoundMetric.class)));
	}

	private MetricConfigurationPanelManualTest(MetricConfiguration configuration) {
		MetricConfigurationPanel panel = new MetricConfigurationPanel();
		panel.set(configuration);
		new ComponentWrapperDialog("MetricConfigurationPanel", panel).setVisible(true);
	}
}