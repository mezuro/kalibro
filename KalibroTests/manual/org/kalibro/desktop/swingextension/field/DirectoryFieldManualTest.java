package org.kalibro.desktop.swingextension.field;

import java.awt.GridLayout;

import javax.swing.JPanel;

import org.kalibro.desktop.ComponentWrapperDialog;

public class DirectoryFieldManualTest {

	public static void main(String[] args) {
		JPanel panel = new DirectoryFieldManualTest().createPanel();
		ComponentWrapperDialog dialog = new ComponentWrapperDialog("DirectoryField", panel);
		dialog.setVisible(true);
		System.exit(0);
	}

	public JPanel createPanel() {
		JPanel panel = new JPanel(new GridLayout(2, 1));
		panel.add(createField());
		panel.add(createField());
		return panel;
	}

	private DirectoryField createField() {
		return new DirectoryField("");
	}
}