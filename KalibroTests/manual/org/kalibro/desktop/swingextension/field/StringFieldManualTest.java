package org.kalibro.desktop.swingextension.field;

import javax.swing.JPanel;

import org.kalibro.desktop.ComponentWrapperDialog;
import org.kalibro.desktop.swingextension.panel.GridBagPanelBuilder;

public class StringFieldManualTest {

	public static void main(String[] args) {
		new ComponentWrapperDialog("StringField", createPanel()).setVisible(true);
	}

	private static JPanel createPanel() {
		GridBagPanelBuilder builder = new GridBagPanelBuilder();
		builder.addSimpleLine(new StringField("", 10));
		builder.addSimpleLine(new StringField("", 10));
		return builder.getPanel();
	}
}