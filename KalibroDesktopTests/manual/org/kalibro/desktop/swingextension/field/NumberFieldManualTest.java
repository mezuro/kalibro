package org.kalibro.desktop.swingextension.field;

import javax.swing.JPanel;

import org.kalibro.desktop.swingextension.panel.GridBagPanelBuilder;
import org.kalibro.desktop.tests.ComponentWrapperDialog;

abstract class NumberFieldManualTest<T extends Number> extends JPanel {

	NumberFieldManualTest() {
		super();
		GridBagPanelBuilder builder = new GridBagPanelBuilder(this);
		builder.addSimpleLine(createField());
		builder.addSimpleLine(createField());
	}

	void execute() {
		new ComponentWrapperDialog(title(), this).setVisible(true);
	}

	abstract String title();

	abstract NumberField<T> createField();
}