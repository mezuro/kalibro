package org.kalibro.desktop.swingextension;

import java.awt.event.ActionListener;

import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

import org.kalibro.desktop.swingextension.field.FieldSize;

public class RadioButton extends JRadioButton {

	public RadioButton(String name, String title, ActionListener... listeners) {
		super(title);
		setName(name);
		setSize(new FieldSize(this));
		setHorizontalAlignment(SwingConstants.LEFT);
		for (ActionListener listener : listeners)
			addActionListener(listener);
	}
}