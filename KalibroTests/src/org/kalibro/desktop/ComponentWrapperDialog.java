package org.kalibro.desktop;

import java.awt.Container;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.kalibro.desktop.swingextension.dialog.AbstractDialog;

public class ComponentWrapperDialog extends AbstractDialog {

	public ComponentWrapperDialog(String title) {
		super(title);
	}

	public ComponentWrapperDialog(String title, JComponent component) {
		super(title);
		setComponent(component);
	}

	public void setComponent(JComponent component) {
		setContentPane(component);
		adjustSize();
		centralize();
	}

	@Override
	protected void createComponents() {
		return;
	}

	@Override
	protected Container buildPanel() {
		return new JPanel();
	}
}