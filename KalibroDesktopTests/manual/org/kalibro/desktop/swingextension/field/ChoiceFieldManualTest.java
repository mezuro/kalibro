package org.kalibro.desktop.swingextension.field;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import org.kalibro.Granularity;
import org.kalibro.desktop.ComponentWrapperDialog;

public final class ChoiceFieldManualTest extends JPanel implements ActionListener {

	public static void main(String[] args) {
		new ComponentWrapperDialog("ChoiceField", new ChoiceFieldManualTest()).setVisible(true);
	}

	private static ChoiceField<Granularity> choiceField;

	private ChoiceFieldManualTest() {
		super(new GridLayout());
		choiceField = new ChoiceField<Granularity>("", Granularity.values());
		choiceField.addActionListener(this);
		add(choiceField);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		System.out.println(choiceField.get());
	}
}