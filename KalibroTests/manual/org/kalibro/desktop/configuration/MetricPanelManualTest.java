package org.kalibro.desktop.configuration;

import org.kalibro.core.model.CompoundMetricFixtures;
import org.kalibro.core.model.NativeMetricFixtures;
import org.kalibro.desktop.ComponentWrapperDialog;

public final class MetricPanelManualTest {

	public static void main(String[] args) {
		MetricPanel panel = new MetricPanel();
		panel.set(NativeMetricFixtures.nativeMetric("amloc"));
		new ComponentWrapperDialog("MetricPanel", panel).setVisible(true);
		panel.set(CompoundMetricFixtures.sc());
		new ComponentWrapperDialog("MetricPanel", panel).setVisible(true);
	}

	private MetricPanelManualTest() {
		// Utility class
	}
}