package org.kalibro.desktop.swingextension.field;

import javax.swing.JCheckBox;
import javax.swing.SwingConstants;

public class BooleanField extends JCheckBox implements Field<Boolean> {

	public BooleanField(String name, String text) {
		super(text);
		setName(name);
		setHorizontalAlignment(SwingConstants.CENTER);
		setSize(new FieldSize(this));
	}

	@Override
	public Boolean get() {
		return isSelected();
	}

	@Override
	public void set(Boolean value) {
		setSelected(value);
	}
}