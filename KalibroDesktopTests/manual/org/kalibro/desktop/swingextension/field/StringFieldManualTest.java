package org.kalibro.desktop.swingextension.field;

import javax.swing.JPanel;

import org.kalibro.desktop.swingextension.panel.GridBagPanelBuilder;
import org.kalibro.desktop.tests.ComponentWrapperDialog;

public final class StringFieldManualTest extends JPanel {

	public static void main(String[] args) {
		new ComponentWrapperDialog("StringField", new StringFieldManualTest()).setVisible(true);
	}

	private StringFieldManualTest() {
		super();
		GridBagPanelBuilder builder = new GridBagPanelBuilder(this);
		builder.addSimpleLine(new StringField("", 15));
		builder.addSimpleLine(new StringField("", 15));
	}
}