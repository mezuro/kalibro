package org.kalibro.desktop;

import java.awt.Container;

import org.kalibro.desktop.swingextension.dialog.AbstractDialog;

public class ComponentWrapperDialog extends AbstractDialog {

	private Container contentPane;

	public ComponentWrapperDialog(String title, Container contentPane) {
		super(title, contentPane);
	}

	@Override
	protected void createComponents(Object... innerComponents) {
		contentPane = (Container) innerComponents[0];
	}

	@Override
	protected Container buildPanel() {
		return contentPane;
	}
}