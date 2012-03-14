package org.kalibro.desktop.swingextension.panel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.kalibro.core.model.enums.Language;
import org.kalibro.desktop.ComponentWrapperDialog;
import org.kalibro.desktop.swingextension.Button;

public final class ConfirmPanelManualTest extends ConfirmPanel<Language> implements ActionListener {

	public static void main(String[] args) {
		new ComponentWrapperDialog("ConfirmPanel", new ConfirmPanelManualTest()).setVisible(true);
	}

	private ConfirmPanelManualTest() {
		super(new LanguagePanelStub());
		addCancelListener(this);
		addOkListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		Button source = (Button) event.getSource();
		System.out.println(source.getName());
	}
}