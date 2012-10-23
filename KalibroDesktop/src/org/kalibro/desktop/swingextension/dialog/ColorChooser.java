package org.kalibro.desktop.swingextension.dialog;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JColorChooser;

public class ColorChooser implements ActionListener {

	private Component parent;
	private Color chosenColor;
	private JColorChooser chooser;

	public ColorChooser(Component parent) {
		this.parent = parent;
		chooser = new JColorChooser();
		chooser.setName("colorChooser");
	}

	public Color chooseColor(Color defaultColor) {
		chosenColor = defaultColor;
		chooser.setColor(defaultColor);
		JColorChooser.createDialog(parent, "Choose color", true, chooser, this, null).setVisible(true);
		return chosenColor;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		chosenColor = chooser.getColor();
	}
}