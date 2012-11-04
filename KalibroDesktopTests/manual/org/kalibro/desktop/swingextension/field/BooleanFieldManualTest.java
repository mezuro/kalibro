package org.kalibro.desktop.swingextension.field;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import org.kalibro.desktop.tests.ComponentWrapperDialog;

public final class BooleanFieldManualTest extends JPanel implements ActionListener {

	public static void main(String[] args) {
		new ComponentWrapperDialog("BooleanField", new BooleanFieldManualTest()).setVisible(true);
	}

	private BooleanField booleanField;

	private BooleanFieldManualTest() {
		super(new GridLayout());
		booleanField = new BooleanField("", "BooleanFieldManualTest");
		booleanField.addActionListener(this);
		add(booleanField);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		System.out.println(booleanField.get());
	}
}