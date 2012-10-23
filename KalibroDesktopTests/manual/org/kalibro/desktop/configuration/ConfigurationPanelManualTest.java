package org.kalibro.desktop.configuration;

import org.kalibro.Configuration;
import org.kalibro.MetricConfiguration;
import org.kalibro.desktop.ComponentWrapperDialog;
import org.kalibro.desktop.swingextension.list.TablePanelListener;
import org.kalibro.tests.UnitTest;

public final class ConfigurationPanelManualTest extends ConfigurationPanel implements
	TablePanelListener<MetricConfiguration> {

	public static void main(String[] args) {
		new ComponentWrapperDialog("ConfigurationPanel", new ConfigurationPanelManualTest()).setVisible(true);
	}

	private ConfigurationPanelManualTest() {
		super();
		set(UnitTest.loadFixture("sc", Configuration.class));
		addMetricConfigurationsListener(this);
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