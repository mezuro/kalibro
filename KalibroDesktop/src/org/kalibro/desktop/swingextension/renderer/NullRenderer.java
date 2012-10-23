package org.kalibro.desktop.swingextension.renderer;

import java.awt.Color;

import javax.swing.JPanel;

public class NullRenderer extends StandardRenderer {

	@Override
	public boolean canRender(Object value) {
		return value == null;
	}

	@Override
	protected JPanel render(Object value) {
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		return panel;
	}
}