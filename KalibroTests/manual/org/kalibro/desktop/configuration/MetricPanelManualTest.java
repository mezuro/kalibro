package org.kalibro.desktop.configuration;

import org.kalibro.core.model.CompoundMetricFixtures;
import org.kalibro.core.model.Metric;
import org.kalibro.core.model.NativeMetricFixtures;
import org.kalibro.desktop.ComponentWrapperDialog;

public final class MetricPanelManualTest {

	public static void main(String[] args) {
		new MetricPanelManualTest(NativeMetricFixtures.nativeMetric("amloc"));
		new MetricPanelManualTest(CompoundMetricFixtures.sc());
	}

	private MetricPanelManualTest(Metric metric) {
		MetricPanel panel = new MetricPanel();
		panel.set(metric);
		new ComponentWrapperDialog("MetricPanel", panel).setVisible(true);
	}
}