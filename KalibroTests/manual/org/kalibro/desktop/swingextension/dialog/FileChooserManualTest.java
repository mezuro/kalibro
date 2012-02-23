package org.kalibro.desktop.swingextension.dialog;

import java.awt.Component;

import org.kalibro.desktop.ComponentWrapperDialog;

public final class FileChooserManualTest {

	public static void main(String[] args) {
		Component parent = new ComponentWrapperDialog("");
		FileChooser fileChooser = new FileChooser(parent);
		System.out.println(fileChooser.chooseFileToSave("FileChooserManualTest.java"));
		System.out.println(fileChooser.getChosenFile());
		System.exit(0);
	}

	private FileChooserManualTest() {
		// Utility class
	}
}