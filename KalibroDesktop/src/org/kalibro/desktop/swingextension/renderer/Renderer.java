package org.kalibro.desktop.swingextension.renderer;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;

public abstract class Renderer {

	protected void changeBackgroundIfSelected(Component component, boolean isSelected) {
		if (! isSelected)
			return;
		Color background = component.getBackground();
		Color defaultSelectionBackground = new JTable().getSelectionBackground();
		Color newBackground = (background == Color.WHITE) ? defaultSelectionBackground : background.darker();
		component.setBackground(newBackground);
	}
}