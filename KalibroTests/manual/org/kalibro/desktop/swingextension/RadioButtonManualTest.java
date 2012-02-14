package org.kalibro.desktop.swingextension;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import org.kalibro.desktop.ComponentWrapperDialog;
import org.kalibro.desktop.swingextension.panel.GridBagPanelBuilder;

public class RadioButtonManualTest implements ActionListener {

	public static void main(String[] args) {
		new ComponentWrapperDialog("RadioButton", createPanel()).setVisible(true);
	}

	private static JPanel createPanel() {
		GridBagPanelBuilder builder = new GridBagPanelBuilder();
		ActionListener listener = new RadioButtonManualTest();
		builder.addSimpleLine(new RadioButton("", "A button", listener));
		builder.add(new RadioButton("", "Another button", listener));
		return builder.getPanel();
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		RadioButton source = (RadioButton) event.getSource();
		System.out.println(source.getText());
	}
}