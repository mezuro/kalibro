package org.kalibro.desktop.swingextension.dialog;

import org.kalibro.desktop.ComponentWrapperDialog;

public final class ErrorDialogManualTest extends ComponentWrapperDialog {

	public static void main(String[] args) {
		Exception error = new Exception("ErrorDialog");
		new ErrorDialog(new ErrorDialogManualTest()).show(error);
		System.exit(0);
	}

	private ErrorDialogManualTest() {
		super("");
	}
}