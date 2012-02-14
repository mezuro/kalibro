package org.kalibro.desktop.configuration;

import org.kalibro.core.model.CompoundMetricFixtures;
import org.kalibro.core.model.NativeMetricFixtures;
import org.kalibro.desktop.ComponentWrapperDialog;

public class MetricPanelManualTest {

	public static void main(String[] args) {
		MetricPanel panel = new MetricPanel();
		panel.show(NativeMetricFixtures.nativeMetric("amloc"));
		new ComponentWrapperDialog("MetricPanel", panel).setVisible(true);
		panel.show(CompoundMetricFixtures.sc());
		new ComponentWrapperDialog("MetricPanel", panel).setVisible(true);
	}
}