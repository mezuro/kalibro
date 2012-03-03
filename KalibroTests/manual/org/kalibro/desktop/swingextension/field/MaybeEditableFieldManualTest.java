package org.kalibro.desktop.swingextension.field;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import org.kalibro.core.model.enums.Granularity;
import org.kalibro.desktop.ComponentWrapperDialog;
import org.kalibro.desktop.swingextension.panel.GridBagPanelBuilder;

public class MaybeEditableFieldManualTest implements ActionListener {

	public static void main(String[] args) {
		new ComponentWrapperDialog("MaybeEditableField", createPanel()).setVisible(true);
	}

	private static JPanel createPanel() {
		GridBagPanelBuilder builder = new GridBagPanelBuilder();
		builder.addSimpleLine(chooseEditable());
		builder.addSimpleLine(maybeEditable());
		return builder.getPanel();
	}

	private static BooleanField chooseEditable() {
		BooleanField chooseEditable = new BooleanField("", "Editable");
		chooseEditable.set(true);
		chooseEditable.addActionListener(new MaybeEditableFieldManualTest());
		return chooseEditable;
	}

	private static MaybeEditableField<Granularity> maybeEditable() {
		ChoiceField<Granularity> editable = new ChoiceField<Granularity>("", Granularity.values());
		return new MaybeEditableField<Granularity>(editable);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		BooleanField source = (BooleanField) event.getSource();
		JPanel parent = (JPanel) source.getParent();
		MaybeEditableField<Granularity> field = (MaybeEditableField<Granularity>) parent.getComponent(1);
		field.setEditable(source.get());
	}
}