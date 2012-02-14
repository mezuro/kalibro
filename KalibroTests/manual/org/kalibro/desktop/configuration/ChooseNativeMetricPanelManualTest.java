package org.kalibro.desktop.configuration;

import org.kalibro.core.model.NativeMetric;
import org.kalibro.desktop.ComponentWrapperDialog;
import org.kalibro.desktop.swingextension.list.ListListener;

public class ChooseNativeMetricPanelManualTest implements ListListener<NativeMetric> {

	public static void main(String[] args) {
		ChooseNativeMetricPanel panel = new ChooseNativeMetricPanel();
		panel.addListListener(new ChooseNativeMetricPanelManualTest());
		new ComponentWrapperDialog("ChooseNativeMetricPanel", panel).setVisible(true);
	}

	@Override
	public void doubleClicked(NativeMetric metric) {
		System.out.println("Double clicked " + metric);
	}

	@Override
	public void selected(NativeMetric metric) {
		System.out.println("Selected " + metric);
	}

	@Override
	public void selectionCleared() {
		System.out.println("Metric selection cleared");
	}
}