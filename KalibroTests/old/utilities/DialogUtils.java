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
}