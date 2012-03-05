package org.kalibro.desktop.configuration;

import static org.kalibro.core.model.ConfigurationFixtures.*;

import org.kalibro.core.model.MetricConfiguration;
import org.kalibro.desktop.ComponentWrapperDialog;
import org.kalibro.desktop.swingextension.list.TablePanelListener;

public class ConfigurationPanelManualTest implements TablePanelListener<MetricConfiguration> {

	public static void main(String[] args) {
		ConfigurationPanel panel = new ConfigurationPanel();
		panel.set(simpleConfiguration());
		panel.addMetricConfigurationsPanelListener(new ConfigurationPanelManualTest());
		new ComponentWrapperDialog("ConfigurationPanel", panel).setVisible(true);
	}

	@Override
	public void add() {
		System.out.println("Add metric configuration");
	}

	@Override
	public void edit(MetricConfiguration metricConfiguration) {
		System.out.println("Edit " + metricConfiguration.getMetric());
	}
}