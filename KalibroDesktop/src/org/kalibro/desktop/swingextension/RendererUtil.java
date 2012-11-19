package org.kalibro.desktop.swingextension;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;

public final class RendererUtil {

	private static final Color DEFAULT_SELECTION_BACKGROUND = new JTable().getSelectionBackground();

	public static void setSelectionBackground(Component component, boolean isSelected) {
		if (isSelected)
			component.setBackground(selectionBackgroundFor(component.getBackground()));
	}

	private static Color selectionBackgroundFor(Color background) {
		return (background == Color.WHITE) ? DEFAULT_SELECTION_BACKGROUND : background.darker();
	}

	private RendererUtil() {
		return;
	}
}