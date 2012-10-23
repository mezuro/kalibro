package org.kalibro.desktop.swingextension.renderer;

import java.awt.Color;
import java.awt.Font;

import org.kalibro.desktop.swingextension.Label;
import org.kalibro.desktop.swingextension.field.DoubleField;

public class DoubleRenderer extends StandardRenderer {

	@Override
	public boolean canRender(Object value) {
		return value instanceof Double;
	}

	@Override
	public Label render(Object value) {
		String text = new DoubleField("").getDecimalFormat().format(value);
		Label label = new Label(text);
		label.setFont(label.getFont().deriveFont(Font.PLAIN));
		label.setBackground(Color.WHITE);
		label.setOpaque(true);
		return label;
	}
}