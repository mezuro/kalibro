package org.kalibro.desktop.swingextension;

import java.awt.event.ActionListener;

import javax.swing.JButton;

import org.kalibro.desktop.swingextension.field.FieldSize;

public class Button extends JButton {

	public Button(String name, String title, ActionListener... listeners) {
		super(title);
		setName(name);
		setSize(new FieldSize(this));
		for (ActionListener listener : listeners)
			addActionListener(listener);
	}
}