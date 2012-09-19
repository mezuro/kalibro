package org.kalibro.desktop.old.utilities;

import java.awt.*;

import javax.swing.Box;
import javax.swing.JDialog;
import javax.swing.JLabel;

public class WaitingDialog extends JDialog {

	private JLabel label;
	private static WaitingDialog dialog;

	private void initDialog(String message) {
		this.getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER));
		this.setResizable(false);

		this.setTitle("Please wait");
		this.setMessage(message);
		Container content = this.getContentPane();
		Container box = Box.createHorizontalBox();
		box.add(label);
		content.add(box, BorderLayout.NORTH);
		this.pack();
	}

	public void setMessage(String message) {
		label = new JLabel(message);
		label.setOpaque(true);
		label.setFont(new Font("Times", 1, 14));
		label.setVisible(true);
	}

	public static void showDialog(String message) {
		dialog = new WaitingDialog();
		dialog.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		dialog.initDialog(message);
		dialog.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - dialog.getSize().width / 2,
			Toolkit.getDefaultToolkit().getScreenSize().height / 2 - dialog.getSize().height / 2);
		dialog.setVisible(true);
		dialog.paint(dialog.getGraphics());
	}

	public static void hideDialog() {
		dialog.setVisible(false);
		dialog = null;
	}

	public static boolean isDialogVisible() {
		if (dialog == null)
			return false;
		return dialog.isVisible();
	}
}