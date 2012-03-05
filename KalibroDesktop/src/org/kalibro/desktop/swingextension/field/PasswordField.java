package org.kalibro.desktop.swingextension.field;

import javax.swing.JPasswordField;
import javax.swing.SwingConstants;

public class PasswordField extends JPasswordField implements Field<String> {

	public PasswordField(String name, int columns) {
		super();
		setName(name);
		setColumns(columns);
		setSize(new FieldSize(this));
		setHorizontalAlignment(SwingConstants.LEFT);
	}

	@Override
	public String get() {
		return new String(getPassword());
	}

	@Override
	public void set(String password) {
		setText(password);
	}
}