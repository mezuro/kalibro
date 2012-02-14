package org.kalibro.desktop.swingextension.field;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import org.kalibro.desktop.ComponentWrapperDialog;

public class BooleanFieldManualTest implements ActionListener {

	public static void main(String[] args) {
		new ComponentWrapperDialog("BooleanField", createPanel()).setVisible(true);
	}

	private static JPanel createPanel() {
		BooleanField field = new BooleanField("", "My boolean field");
		field.addActionListener(new BooleanFieldManualTest());

		JPanel panel = new JPanel(new GridLayout(1, 1));
		panel.add(field);
		return panel;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		BooleanField field = (BooleanField) event.getSource();
		System.out.println(field.getValue());
	}
}