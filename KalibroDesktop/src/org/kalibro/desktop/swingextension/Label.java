package org.kalibro.desktop.swingextension;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import org.kalibro.desktop.swingextension.field.FieldSize;

public class Label extends JLabel {

	public Label(String title) {
		super(title, SwingConstants.RIGHT);
		setSize(new FieldSize(this));
	}
}