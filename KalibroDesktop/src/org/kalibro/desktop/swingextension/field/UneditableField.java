package org.kalibro.desktop.swingextension.field;

import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class UneditableField<T> extends JLabel implements Field<T> {

	private T fieldValue;

	public UneditableField(String name) {
		super("", SwingConstants.LEFT);
		setFont(getFont().deriveFont(Font.PLAIN));
		setName(name);
		adjustSize();
	}

	@Override
	public T get() {
		return fieldValue;
	}

	@Override
	public void set(T value) {
		fieldValue = value;
		setText((value == null) ? "" : value.toString());
		adjustSize();
	}

	private void adjustSize() {
		setPreferredSize(new JLabel("" + fieldValue).getPreferredSize());
		setSize(new FieldSize(this));
	}
}