package org.kalibro.desktop.swingextension.field;

import javax.swing.JPanel;

import org.kalibro.desktop.swingextension.panel.GridBagPanelBuilder;
import org.kalibro.desktop.tests.ComponentWrapperDialog;

public final class PasswordFieldManualTest extends JPanel {

	public static void main(String[] args) {
		new ComponentWrapperDialog("PasswordField", new PasswordFieldManualTest()).setVisible(true);
	}

	private PasswordFieldManualTest() {
		super();
		GridBagPanelBuilder builder = new GridBagPanelBuilder(this);
		builder.addSimpleLine(new PasswordField("", 10));
		builder.addSimpleLine(new PasswordField("", 10));
	}
}