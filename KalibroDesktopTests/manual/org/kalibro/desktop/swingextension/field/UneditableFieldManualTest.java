package org.kalibro.desktop.swingextension.field;

import javax.swing.JPanel;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import org.kalibro.desktop.swingextension.panel.GridBagPanelBuilder;
import org.kalibro.desktop.tests.ComponentWrapperDialog;

public final class UneditableFieldManualTest extends JPanel implements CaretListener {

	public static void main(String[] args) {
		new ComponentWrapperDialog("UneditableField", new UneditableFieldManualTest()).setVisible(true);
	}

	private StringField editableField;
	private UneditableField<String> uneditableField;

	private UneditableFieldManualTest() {
		super();
		editableField = new StringField("", 10);
		editableField.addCaretListener(this);
		uneditableField = new UneditableField<String>("");

		GridBagPanelBuilder builder = new GridBagPanelBuilder(this);
		builder.addSimpleLine(editableField);
		builder.addSimpleLine(uneditableField);
	}

	@Override
	public void caretUpdate(CaretEvent event) {
		uneditableField.set(editableField.get());
	}
}