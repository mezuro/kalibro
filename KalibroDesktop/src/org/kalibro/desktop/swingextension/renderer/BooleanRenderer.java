package org.kalibro.desktop.swingextension.renderer;

import java.awt.Color;

import org.kalibro.desktop.swingextension.field.BooleanField;

public class BooleanRenderer extends StandardRenderer {

	@Override
	public boolean canRender(Object value) {
		return value instanceof Boolean;
	}

	@Override
	public BooleanField render(Object value) {
		BooleanField booleanField = new BooleanField("", "");
		booleanField.setBackground(Color.WHITE);
		booleanField.setSelected((Boolean) value);
		return booleanField;
	}
}