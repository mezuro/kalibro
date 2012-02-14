package org.kalibro.desktop.swingextension.field;

import java.awt.GridLayout;

import javax.swing.JPanel;

import org.kalibro.desktop.ComponentWrapperDialog;

public class DirectoryFieldManualTest {

	public static void main(String[] args) {
		ComponentWrapperDialog dialog = new ComponentWrapperDialog("DirectoryField", createPanel());
		dialog.setVisible(true);
		System.exit(0);
	}

	private static JPanel createPanel() {
		JPanel panel = new JPanel(new GridLayout(2, 1));
		panel.add(createField());
		panel.add(createField());
		return panel;
	}

	private static DirectoryField createField() {
		return new DirectoryField("");
	}
}