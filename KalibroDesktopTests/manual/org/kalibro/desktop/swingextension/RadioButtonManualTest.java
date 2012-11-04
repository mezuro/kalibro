package org.kalibro.desktop.swingextension;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import org.kalibro.desktop.swingextension.panel.GridBagPanelBuilder;
import org.kalibro.desktop.tests.ComponentWrapperDialog;

public final class RadioButtonManualTest extends JPanel implements ActionListener {

	public static void main(String[] args) {
		new ComponentWrapperDialog("RadioButton", new RadioButtonManualTest()).setVisible(true);
	}

	private RadioButtonManualTest() {
		super();
		GridBagPanelBuilder builder = new GridBagPanelBuilder(this);
		builder.addSimpleLine(new RadioButton("", "A button", this));
		builder.add(new RadioButton("", "Another button", this));
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		RadioButton source = (RadioButton) event.getSource();
		System.out.println(source.getText());
	}
}