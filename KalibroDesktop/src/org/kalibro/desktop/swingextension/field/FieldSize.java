package org.kalibro.desktop.swingextension.field;

import java.awt.Component;
import java.awt.Dimension;

public class FieldSize extends Dimension {

	public FieldSize(Component component) {
		setSize(component.getPreferredSize().width, 2 * component.getFont().getSize());
		component.setMinimumSize(this);
		component.setPreferredSize(this);
	}
}