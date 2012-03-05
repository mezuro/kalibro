package org.kalibro.desktop.swingextension.field;

import java.awt.GridLayout;

import javax.swing.JPanel;

import org.kalibro.desktop.ComponentWrapperDialog;

public final class DirectoryFieldManualTest extends JPanel {

	public static void main(String[] args) {
		new ComponentWrapperDialog("DirectoryField", new DirectoryFieldManualTest()).setVisible(true);
		System.exit(0);
	}

	private DirectoryFieldManualTest() {
		super(new GridLayout(2, 1));
		add(createField());
		add(createField());
	}

	private DirectoryField createField() {
		return new DirectoryField("");
	}
}