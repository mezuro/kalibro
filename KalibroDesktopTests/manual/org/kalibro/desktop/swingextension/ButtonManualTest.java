package org.kalibro.desktop.swingextension;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import org.kalibro.desktop.swingextension.panel.GridBagPanelBuilder;
import org.kalibro.desktop.tests.ComponentWrapperDialog;

public final class ButtonManualTest extends JPanel implements ActionListener {

	public static void main(String[] args) {
		new ComponentWrapperDialog("Button", new ButtonManualTest()).setVisible(true);
	}

	private ButtonManualTest() {
		super();
		GridBagPanelBuilder builder = new GridBagPanelBuilder(this);
		builder.addSimpleLine(new Button("", "A button", this));
		builder.add(new Button("", "Another button", this));
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		Button source = (Button) event.getSource();
		System.out.println(source.getText());
	}
}