package org.kalibro.desktop.swingextension.field;

import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class StringField extends JTextField implements Field<String> {

	public StringField(String name, int columns) {
		super();
		setName(name);
		setColumns(columns);
		setSize(new FieldSize(this));
		setHorizontalAlignment(SwingConstants.LEFT);
	}

	@Override
	public String get() {
		return getText().trim();
	}

	@Override
	public void set(String value) {
		setText(value.trim());
	}
}