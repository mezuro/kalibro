package org.kalibro.desktop.configuration;

import static org.kalibro.core.model.MetricFixtures.*;

import org.kalibro.core.model.Metric;
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