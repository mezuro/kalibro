package org.kalibro.desktop.swingextension.dialog;

public final class InputDialogManualTest {

	public static void main(String[] args) {
		InputDialog inputDialog = new InputDialog(null, "InputDialog");
		if (inputDialog.userTyped("Type something:"))
			System.out.println(inputDialog.getInput());
		else
			System.out.println("Cancelled");
	}

	private InputDialogManualTest() {
		return;
	}
}