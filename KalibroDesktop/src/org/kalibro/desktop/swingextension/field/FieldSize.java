package org.kalibro.desktop.swingextension.field;

import java.awt.Dimension;

import javax.swing.JComponent;

public class FieldSize extends Dimension {

	public FieldSize(JComponent component) {
		setSize(component.getPreferredSize().width, 2 * component.getFont().getSize());
		component.setMinimumSize(this);
		component.setPreferredSize(this);
	}
}