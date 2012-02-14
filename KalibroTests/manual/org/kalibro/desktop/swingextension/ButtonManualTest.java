package org.kalibro.desktop.swingextension;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import org.kalibro.desktop.ComponentWrapperDialog;
import org.kalibro.desktop.swingextension.panel.GridBagPanelBuilder;

public class ButtonManualTest implements ActionListener {

	public static void main(String[] args) {
		new ComponentWrapperDialog("Button", createPanel()).setVisible(true);
	}

	private static JPanel createPanel() {
		GridBagPanelBuilder builder = new GridBagPanelBuilder();
		ActionListener listener = new ButtonManualTest();
		builder.addSimpleLine(new Button("", "A button", listener));
		builder.add(new Button("", "Another button", listener));
		return builder.getPanel();
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		Button source = (Button) event.getSource();
		System.out.println(source.getText());
	}
}