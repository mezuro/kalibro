package org.kalibro.desktop.swingextension.renderer;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class StringRenderer extends StandardRenderer {

	@Override
	public boolean canRender(Object value) {
		return value instanceof String;
	}

	@Override
	public JLabel render(Object value) {
		JLabel label = new JLabel((String) value);
		label.setFont(label.getFont().deriveFont(Font.PLAIN));
		label.setHorizontalAlignment(SwingConstants.LEFT);
		label.setBackground(Color.WHITE);
		label.setOpaque(true);
		return label;
	}
}