package org.kalibro.desktop.swingextension;

import java.awt.event.ActionListener;

import javax.swing.JButton;

import org.kalibro.desktop.swingextension.field.FieldSize;

public class Button extends JButton {

	public Button(String name, String text, ActionListener... listeners) {
		super();
		initialize(name, text);
		for (ActionListener listener : listeners)
			addActionListener(listener);
	}

	public Button(String name, String text, Object target, String methodName, Object... arguments) {
		super();
		setAction(new ReflectiveAction(target, methodName, arguments));
		initialize(name, text);
	}

	private void initialize(String name, String text) {
		setName(name);
		setText(text);
		setSize(new FieldSize(this));
	}
}