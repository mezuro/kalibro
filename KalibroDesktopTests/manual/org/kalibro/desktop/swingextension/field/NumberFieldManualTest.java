package org.kalibro.desktop.swingextension.field;

import javax.swing.JPanel;

import org.kalibro.desktop.ComponentWrapperDialog;
import org.kalibro.desktop.swingextension.panel.GridBagPanelBuilder;

abstract class NumberFieldManualTest<T extends Number> extends JPanel {

	protected NumberFieldManualTest() {
		super();
		GridBagPanelBuilder builder = new GridBagPanelBuilder(this);
		builder.addSimpleLine(normalField());
		builder.addSimpleLine(specialNumberField());
	}

	public void execute() {
		new ComponentWrapperDialog(title(), this).setVisible(true);
	}

	protected abstract String title();

	protected abstract NumberField<T> normalField();

	protected abstract NumberField<T> specialNumberField();
}