package org.kalibro.desktop.swingextension.dialog;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;

import org.kalibro.desktop.tests.ComponentWrapperDialog;

public final class FileChooserManualTest extends JLabel {

	public static void main(String[] args) {
		new ComponentWrapperDialog("FileChooser", new FileChooserManualTest()).setVisible(true);
	}

	private FileChooser fileChooser;

	private FileChooserManualTest() {
		super("Click");
		fileChooser = new FileChooser(this);
		setPreferredSize(new Dimension(640, 30));
		addMouseListener(new ClickAction());
	}

	private class ClickAction extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent event) {
			if (fileChooser.chooseFileToSave("FileChooserManualTest.java"))
				setText(fileChooser.getChosenFile().getAbsolutePath());
			else
				setText("Cancelled");
		}
	}
}