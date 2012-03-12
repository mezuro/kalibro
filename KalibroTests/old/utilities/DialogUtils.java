package org.kalibro.desktop.old.utilities;

import static javax.swing.JOptionPane.*;

import java.awt.Component;

import javax.swing.JOptionPane;

public final class DialogUtils {

	private DialogUtils() {
		// instantiation forbidden
	}

	public static void showWaitingDialog(String message) {
		WaitingDialog.showDialog(message);
	}

	public static void hideWaitingDialog() {
		WaitingDialog.hideDialog();
	}

	public static int yesNoCancelConfirmation(Component parent, String message, String title) {
		return showConfirmDialog(parent, message, title, JOptionPane.YES_NO_CANCEL_OPTION);
	}
}