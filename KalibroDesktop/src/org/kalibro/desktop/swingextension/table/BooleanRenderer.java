package org.kalibro.desktop.swingextension.table;

import java.awt.Color;

import org.kalibro.desktop.swingextension.field.BooleanField;

class BooleanRenderer extends StandardRenderer {

	@Override
	boolean canRender(Object value) {
		return value instanceof Boolean;
	}

	@Override
	BooleanField render(Object value) {
		BooleanField booleanField = new BooleanField("", "");
		booleanField.setBackground(Color.WHITE);
		booleanField.setSelected((Boolean) value);
		return booleanField;
	}
}