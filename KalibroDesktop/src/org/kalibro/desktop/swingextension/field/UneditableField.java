package org.kalibro.desktop.swingextension.field;

import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class UneditableField<T> extends JLabel implements Field<T> {

	private T value;

	public UneditableField(String name) {
		super("", SwingConstants.LEFT);
		setName(name);
		setFont(getFont().deriveFont(Font.PLAIN));
		adjustSize();
	}

	@Override
	public T getValue() {
		return value;
	}

	@Override
	public void setValue(T value) {
		this.value = value;
		setText((value == null) ? "" : value.toString());
		adjustSize();
	}

	private void adjustSize() {
		setPreferredSize(new JLabel("" + value).getPreferredSize());
		setSize(new FieldSize(this));
	}
}