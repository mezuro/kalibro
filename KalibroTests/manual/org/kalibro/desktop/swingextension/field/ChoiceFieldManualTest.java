package org.kalibro.desktop.swingextension.field;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import org.kalibro.core.model.enums.Granularity;
import org.kalibro.desktop.ComponentWrapperDialog;

public class ChoiceFieldManualTest implements ActionListener {

	public static void main(String[] args) {
		new ComponentWrapperDialog("ChoiceField", createPanel()).setVisible(true);
	}

	private static JPanel createPanel() {
		ChoiceField<Granularity> field = new ChoiceField<Granularity>("", Granularity.values());
		field.addActionListener(new ChoiceFieldManualTest());

		JPanel panel = new JPanel(new GridLayout(1, 1));
		panel.add(field);
		return panel;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		ChoiceField<Granularity> field = (ChoiceField<Granularity>) event.getSource();
		System.out.println(field.getValue());
	}
}