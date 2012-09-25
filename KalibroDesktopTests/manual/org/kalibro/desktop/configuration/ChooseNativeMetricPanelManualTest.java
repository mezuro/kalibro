package org.kalibro.desktop.configuration;

import org.kalibro.NativeMetric;
import org.kalibro.desktop.ComponentWrapperDialog;
import org.kalibro.desktop.swingextension.list.ListListener;

public final class ChooseNativeMetricPanelManualTest extends ChooseNativeMetricPanel implements
	ListListener<NativeMetric> {

	public static void main(String[] args) {
		new ComponentWrapperDialog("ChooseNativeMetricPanel", new ChooseNativeMetricPanelManualTest()).setVisible(true);
	}

	private ChooseNativeMetricPanelManualTest() {
		super();
		addListListener(this);
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