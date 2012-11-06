package org.kalibro.desktop.swingextension.renderer;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;

abstract class Renderer {

	private static final Color DEFAULT_SELECTION_BACKGROUND = new JTable().getSelectionBackground();

	protected void setSelectionBackground(Component component, boolean isSelected) {
		if (isSelected)
			component.setBackground(selectionBackgroundFor(component.getBackground()));
	}

	private Color selectionBackgroundFor(Color background) {
		return (background == Color.WHITE) ? DEFAULT_SELECTION_BACKGROUND : background.darker();
	}
}