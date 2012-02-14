package org.kalibro.desktop.configuration;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddMetricDialogManualTest implements ActionListener {

	private static AddMetricDialog dialog;

	public static void main(String[] args) {
		dialog = new AddMetricDialog();
		dialog.addOkListener(new AddMetricDialogManualTest());
		dialog.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		dialog.dispose();
		System.out.println("Metric: " + dialog.getMetric());
	}
}