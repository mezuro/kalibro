package org.kalibro.desktop.swingextension.field;

import java.awt.GridLayout;

import javax.swing.JPanel;

import org.kalibro.desktop.ComponentWrapperDialog;

public class ColorFieldManualTest {

	public static void main(String[] args) {
		JPanel panel = new ColorFieldManualTest().createPanel();
		new ComponentWrapperDialog("ColorField", panel).setVisible(true);
	}

	public JPanel createPanel() {
		JPanel panel = new JPanel(new GridLayout(1, 1));
		panel.add(new ColorField(""));
		return panel;
	}
}