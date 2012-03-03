package org.kalibro.desktop.swingextension.field;

import javax.swing.JPanel;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import org.kalibro.desktop.ComponentWrapperDialog;
import org.kalibro.desktop.swingextension.panel.GridBagPanelBuilder;

public class UneditableFieldManualTest implements CaretListener {

	public static void main(String[] args) {
		new ComponentWrapperDialog("UneditableField", createPanel()).setVisible(true);
	}

	private static JPanel createPanel() {
		GridBagPanelBuilder builder = new GridBagPanelBuilder();
		builder.addSimpleLine(stringField());
		builder.addSimpleLine(new UneditableField<String>(""));
		return builder.getPanel();
	}

	private static StringField stringField() {
		StringField stringField = new StringField("", 10);
		stringField.addCaretListener(new UneditableFieldManualTest());
		return stringField;
	}

	@Override
	public void caretUpdate(CaretEvent event) {
		StringField source = (StringField) event.getSource();
		JPanel parent = (JPanel) source.getParent();
		UneditableField<String> field = (UneditableField<String>) parent.getComponent(1);
		field.set(source.get());
	}
}