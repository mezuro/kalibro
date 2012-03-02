package org.kalibro.desktop.swingextension.field;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.kalibro.desktop.swingextension.Button;
import org.kalibro.desktop.swingextension.dialog.ColorChooser;

public class ColorField extends Button implements ActionListener, Field<Color> {

	private ColorChooser chooser;

	public ColorField(String name) {
		super(name, "Choose color");
		addActionListener(this);
		setBackground(Color.WHITE);
		chooser = new ColorChooser(this);
	}

	@Override
	public Color getValue() {
		return getBackground();
	}

	@Override
	public void setValue(Color color) {
		setBackground(color);
		setForeground(new Color(Integer.MAX_VALUE - color.getRGB()));
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		setValue(chooser.chooseColor(getValue()));
	}
}