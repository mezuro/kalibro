package org.kalibro.desktop.swingextension.dialog;

import java.awt.*;

import javax.swing.JDialog;

import org.kalibro.desktop.swingextension.icon.Icon;

public abstract class AbstractDialog extends JDialog {

	public AbstractDialog(String title, Component... innerComponents) {
		super((Frame) null, title, true);
		setIconImage(new Icon(Icon.KALIBRO).getImage());
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		createComponents(innerComponents);
		setContentPane(buildPanel());
		adjustSize();
		centralize();
	}

	protected abstract void createComponents(Component... innerComponents);

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