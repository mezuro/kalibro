package org.kalibro.desktop.configuration;

import static org.kalibro.tests.UnitTest.loadFixture;

import org.kalibro.CompoundMetric;
import org.kalibro.Metric;
import org.kalibro.NativeMetric;
import org.kalibro.desktop.tests.ComponentWrapperDialog;

@Deprecated
public final class MetricPanelManualTest {

	public static void main(String[] args) {
		new MetricPanelManualTest(loadFixture("lcom4", NativeMetric.class));
		new MetricPanelManualTest(loadFixture("sc", CompoundMetric.class));
	}

	private MetricPanelManualTest(Metric metric) {
		MetricPanel panel = new MetricPanel();
		panel.set(metric);
		new ComponentWrapperDialog("MetricPanel", panel).setVisible(true);
	}
}