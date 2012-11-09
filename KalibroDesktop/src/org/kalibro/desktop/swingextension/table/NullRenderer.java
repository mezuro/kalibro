package org.kalibro.desktop.swingextension.table;

import java.awt.Color;

import javax.swing.JPanel;

class NullRenderer extends StandardRenderer {

	@Override
	boolean canRender(Object value) {
		return value == null;
	}

	@Override
	JPanel render(Object value) {
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		return panel;
	}
}