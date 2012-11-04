package org.kalibro.desktop.swingextension.field;

import java.awt.GridLayout;

import javax.swing.JPanel;

import org.kalibro.desktop.tests.ComponentWrapperDialog;

public final class ColorFieldManualTest extends JPanel {

	public static void main(String[] args) {
		new ComponentWrapperDialog("ColorField", new ColorFieldManualTest()).setVisible(true);
	}

	private ColorFieldManualTest() {
		super(new GridLayout());
		add(new ColorField(""));
	}
}