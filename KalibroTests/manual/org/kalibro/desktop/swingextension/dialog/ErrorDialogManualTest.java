package org.kalibro.desktop.swingextension.dialog;

import java.awt.Component;

import org.kalibro.desktop.ComponentWrapperDialog;

public final class ErrorDialogManualTest {

	public static void main(String[] args) {
		Component parent = new ComponentWrapperDialog("");
		Exception error = new Exception("Manual testing of ErrorDialog");
		new ErrorDialog(parent).show(error);
		System.exit(0);
	}

	private ErrorDialogManualTest() {
		// Utility class
	}
}