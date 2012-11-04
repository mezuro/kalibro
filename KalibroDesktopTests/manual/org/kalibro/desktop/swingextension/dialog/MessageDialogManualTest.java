package org.kalibro.desktop.swingextension.dialog;

import javax.swing.JPanel;

import org.kalibro.desktop.tests.ComponentWrapperDialog;

public final class MessageDialogManualTest extends ComponentWrapperDialog {

	public static void main(String[] args) {
		new MessageDialog("MessageDialog", new MessageDialogManualTest()).show("MessageDialogManualTest");
		System.exit(0);
	}

	private MessageDialogManualTest() {
		super("", new JPanel());
	}
}