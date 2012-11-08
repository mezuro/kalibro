package org.kalibro.desktop.swingextension.dialog;

public final class ErrorDialogManualTest {

	public static void main(String[] args) {
		new ErrorDialog(null).show(new Exception("ErrorDialog"));
	}

	private ErrorDialogManualTest() {
		return;
	}
}