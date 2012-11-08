package org.kalibro.desktop.swingextension.dialog;

public final class MessageDialogManualTest {

	public static void main(String[] args) {
		new MessageDialog(null, "MessageDialog").show("MessageDialogManualTest message");
	}

	private MessageDialogManualTest() {
		return;
	}
}