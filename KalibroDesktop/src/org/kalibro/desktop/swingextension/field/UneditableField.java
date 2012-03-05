package org.kalibro.desktop.swingextension.field;

import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class UneditableField<T> extends JLabel implements Field<T> {

	private T theValue;

	public UneditableField(String name) {
		super("", SwingConstants.LEFT);
		setName(name);
		setFont(getFont().deriveFont(Font.PLAIN));
		adjustSize();
	}

	@Override
	public T get() {
		return theValue;
	}

	@Override
	public void set(T value) {
		theValue = value;
		setText((value == null) ? "" : value.toString());
		adjustSize();
	}

	private void adjustSize() {
		setPreferredSize(new JLabel("" + theValue).getPreferredSize());
		setSize(new FieldSize(this));
	}
}