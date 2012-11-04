package org.kalibro.desktop.swingextension.dialog;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;

import org.kalibro.Language;
import org.kalibro.desktop.tests.ComponentWrapperDialog;

public final class ChoiceDialogManualTest extends JLabel {

	public static void main(String[] args) {
		new ComponentWrapperDialog("ChoiceDialog", new ChoiceDialogManualTest()).setVisible(true);
	}

	private ChoiceDialog<Language> choiceDialog;

	private ChoiceDialogManualTest() {
		super("Click");
		choiceDialog = new ChoiceDialog<Language>("Choose language", this);
		setPreferredSize(new Dimension(400, 30));
		addMouseListener(new ClickAction());
	}

	private class ClickAction extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent event) {
			if (choiceDialog.choose("Please select the language:", Language.values()))
				setText("" + choiceDialog.getChoice());
			else
				setText("Cancelled");
		}
	}
}