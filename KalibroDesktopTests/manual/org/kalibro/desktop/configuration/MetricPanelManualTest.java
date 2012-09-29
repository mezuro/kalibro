package org.kalibro.desktop.configuration;

import static org.kalibro.MetricFixtures.*;

import org.kalibro.Metric;
import org.kalibro.desktop.ComponentWrapperDialog;

public final class MetricPanelManualTest {

	public static void main(String[] args) {
		new MetricPanelManualTest(analizoMetric("amloc"));
		new MetricPanelManualTest(sc());
	}

	private MetricPanelManualTest(Metric metric) {
		MetricPanel panel = new MetricPanel();
		panel.set(metric);
		new ComponentWrapperDialog("MetricPanel", panel).setVisible(true);
	}
}