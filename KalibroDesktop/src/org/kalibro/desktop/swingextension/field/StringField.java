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
	public String getText() {
		return super.getText().trim();
	}

	@Override
	public String getValue() {
		return getText();
	}

	@Override
	public void setValue(String value) {
		setText(value);
	}
}