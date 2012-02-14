package org.kalibro.desktop.swingextension.field;

import javax.swing.JPanel;

import org.kalibro.desktop.ComponentWrapperDialog;
import org.kalibro.desktop.swingextension.panel.GridBagPanelBuilder;

public abstract class NumberFieldManualTest<T extends Number> {

	public void execute() {
		new ComponentWrapperDialog(title(), createPanel()).setVisible(true);
	}

	private JPanel createPanel() {
		GridBagPanelBuilder builder = new GridBagPanelBuilder();
		builder.addSimpleLine(normalField());
		builder.addSimpleLine(specialNumberField());
		return builder.getPanel();
	}

	protected abstract String title();

	protected abstract NumberField<T> normalField();

	protected abstract NumberField<T> specialNumberField();
}