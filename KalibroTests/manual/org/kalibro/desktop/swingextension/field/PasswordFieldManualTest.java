package org.kalibro.desktop.swingextension.field;

import javax.swing.JPanel;

import org.kalibro.desktop.ComponentWrapperDialog;
import org.kalibro.desktop.swingextension.panel.GridBagPanelBuilder;

public class PasswordFieldManualTest {

	public static void main(String[] args) {
		new ComponentWrapperDialog("PasswordField", createPanel()).setVisible(true);
	}

	private static JPanel createPanel() {
		GridBagPanelBuilder builder = new GridBagPanelBuilder();
		builder.addSimpleLine(new PasswordField("", 10));
		builder.addSimpleLine(new PasswordField("", 10));
		return builder.getPanel();
	}
}