package org.kalibro.desktop.swingextension.dialog;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import org.kalibro.desktop.ComponentWrapperDialog;

public final class ColorChooserManualTest extends JPanel {

	public static void main(String[] args) {
		new ComponentWrapperDialog("ColorChooserManualTest", new ColorChooserManualTest()).setVisible(true);
		System.exit(0);
	}

	private ColorChooser colorChooser;

	private ColorChooserManualTest() {
		super();
		colorChooser = new ColorChooser(this);
		setPreferredSize(new Dimension(640, 480));
		addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent event) {
				setBackground(colorChooser.chooseColor(getBackground()));
			}
		});
	}
}