package org.kalibro.desktop.swingextension.dialog;

import java.awt.*;

import javax.swing.JDialog;

public abstract class AbstractDialog extends JDialog {

	private static boolean suppressShow;

	public AbstractDialog(Window owner, String title, Component... innerComponents) {
		super(owner, title);
		setModal(true);
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

	@Override
	public void setVisible(boolean visible) {
		if (suppressShow)
			suppressShow = false;
		else
			super.setVisible(visible);
	}
}