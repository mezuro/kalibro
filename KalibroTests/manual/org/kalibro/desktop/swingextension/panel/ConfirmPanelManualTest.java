package org.kalibro.desktop.swingextension.panel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.kalibro.desktop.ComponentWrapperDialog;
import org.kalibro.desktop.swingextension.Button;
import org.kalibro.desktop.swingextension.field.TextField;

public final class ConfirmPanelManualTest extends ConfirmPanel implements ActionListener {

	public static void main(String[] args) {
		new ComponentWrapperDialog("ConfirmPanel", new ConfirmPanelManualTest()).setVisible(true);
	}

	private ConfirmPanelManualTest() {
		super(new TextField("", 5, 20));
		addCancelListener(this);
		addOkListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		Button source = (Button) event.getSource();
		System.out.println(source.getName());
	}
}