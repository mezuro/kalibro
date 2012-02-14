package org.kalibro.desktop.swingextension.dialog;

import java.awt.Color;
import java.awt.Component;

import org.kalibro.desktop.ComponentWrapperDialog;

public class ColorChooserManualTest {

	public static void main(String[] args) {
		Component parent = new ComponentWrapperDialog("");
		ColorChooser colorChooser = new ColorChooser(parent);
		System.out.println(colorChooser.chooseColor(Color.BLACK));
		System.exit(0);
	}
}