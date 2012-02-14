package org.kalibro.desktop.swingextension.dialog;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;

import javax.swing.JDialog;

import org.kalibro.desktop.icon.KalibroIcon;

public abstract class AbstractDialog extends JDialog {

	public AbstractDialog(String title) {
		super((Frame) null, title, true);
		setIconImage(new KalibroIcon().getImage());
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		createComponents();
		setContentPane(buildPanel());
		adjustSize();
		centralize();
	}

	protected abstract void createComponents();

	protected abstract Container buildPanel();

	public void adjustSize() {
		pack();
		setMinimumSize(getPreferredSize());
		setSize(getPreferredSize());
	}

	protected void centralize() {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension screenSize = toolkit.getScreenSize();
		setLocation(screenSize.width / 2 - getSize().width / 2, screenSize.height / 2 - getSize().height / 2);
	}
}