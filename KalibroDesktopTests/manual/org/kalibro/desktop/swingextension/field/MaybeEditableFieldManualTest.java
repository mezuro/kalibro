package org.kalibro.desktop.swingextension.field;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import org.kalibro.Granularity;
import org.kalibro.desktop.ComponentWrapperDialog;
import org.kalibro.desktop.swingextension.panel.GridBagPanelBuilder;

public final class MaybeEditableFieldManualTest extends JPanel implements ActionListener {

	public static void main(String[] args) {
		new ComponentWrapperDialog("MaybeEditableField", new MaybeEditableFieldManualTest()).setVisible(true);
	}

	private BooleanField chooseEditable;
	private MaybeEditableField<Granularity> field;

	private MaybeEditableFieldManualTest() {
		super();
		initializeComponents();
		GridBagPanelBuilder builder = new GridBagPanelBuilder(this);
		builder.addSimpleLine(chooseEditable);
		builder.addSimpleLine(field);
	}

	private void initializeComponents() {
		chooseEditable = new BooleanField("", "Editable");
		chooseEditable.set(true);
		chooseEditable.addActionListener(this);
		field = new MaybeEditableField<Granularity>(new ChoiceField<Granularity>("", Granularity.values()));
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		field.setEditable(chooseEditable.get());
	}
}