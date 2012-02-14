package org.kalibro.desktop.swingextension.dialog;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JColorChooser;

public class ColorChooser {

	private Component parent;
	private JColorChooser chooser;

	public ColorChooser(Component parent) {
		this.parent = parent;
		chooser = new JColorChooser();
		chooser.setName("colorChooser");
	}

	public Color chooseColor(Color defaultColor) {
		ColorChooserListener listener = new ColorChooserListener(defaultColor);
		JColorChooser.createDialog(parent, "Choose color", true, chooser, listener, null).setVisible(true);
		return listener.getChosenColor();
	}

	private class ColorChooserListener implements ActionListener {

		private Color chosenColor;

		private ColorChooserListener(Color defaultColor) {
			chooser.setColor(defaultColor);
			chosenColor = defaultColor;
		}

		public Color getChosenColor() {
			return chosenColor;
		}

		@Override
		public void actionPerformed(ActionEvent event) {
			chosenColor = chooser.getColor();
		}
	}
}