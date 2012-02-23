package org.kalibro.desktop.swingextension;

import java.awt.GridLayout;

import javax.swing.JPanel;

import org.kalibro.desktop.ComponentWrapperDialog;

public final class LabelManualTest {

	public static void main(String[] args) {
		new ComponentWrapperDialog("Label", createPanel()).setVisible(true);
	}

	private static JPanel createPanel() {
		JPanel panel = new JPanel(new GridLayout(1, 1));
		panel.add(new Label("My label"));
		return panel;
	}

	private LabelManualTest() {
		// Utility class
	}
}