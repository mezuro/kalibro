package org.kalibro.desktop.swingextension.dialog;

import java.awt.*;

import javax.swing.JDialog;

public abstract class AbstractDialog extends JDialog {

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

	private void centralize() {
		Dimension dialogSize = getSize();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((screenSize.width - dialogSize.width) / 2, (screenSize.height - dialogSize.height) / 2);
	}
}