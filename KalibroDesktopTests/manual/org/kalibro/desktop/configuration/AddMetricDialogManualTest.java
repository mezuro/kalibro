package org.kalibro.desktop.configuration;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public final class AddMetricDialogManualTest implements ActionListener {

	public static void main(String[] args) {
		new AddMetricDialogManualTest();
	}

	private AddMetricDialog dialog;

	private AddMetricDialogManualTest() {
		dialog = new AddMetricDialog();
		dialog.addOkListener(this);
		dialog.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		dialog.dispose();
		System.out.println("Metric: " + dialog.getMetric());
	}
}