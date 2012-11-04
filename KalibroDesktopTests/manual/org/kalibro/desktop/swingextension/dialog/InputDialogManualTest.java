package org.kalibro.desktop.swingextension.dialog;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;

import org.kalibro.desktop.tests.ComponentWrapperDialog;

public final class InputDialogManualTest extends JLabel {

	public static void main(String[] args) {
		new ComponentWrapperDialog("InputDialog", new InputDialogManualTest()).setVisible(true);
	}

	private InputDialog inputDialog;

	private InputDialogManualTest() {
		super("Click");
		inputDialog = new InputDialog("Input", this);
		setPreferredSize(new Dimension(400, 30));
		addMouseListener(new ClickAction());
	}

	private class ClickAction extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent event) {
			if (inputDialog.userTyped("Type something:"))
				setText("" + inputDialog.getInput());
			else
				setText("Cancelled");
		}
	}
}