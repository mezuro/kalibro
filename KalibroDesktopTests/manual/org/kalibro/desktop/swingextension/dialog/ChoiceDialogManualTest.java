package org.kalibro.desktop.swingextension.dialog;

import org.kalibro.Language;

public final class ChoiceDialogManualTest {

	public static void main(String[] args) {
		ChoiceDialog<Language> choiceDialog = new ChoiceDialog<Language>(null, "ChoiceDialog");
		if (choiceDialog.choose("Please select the language:", Language.values()))
			System.out.println(choiceDialog.getChoice());
		else
			System.out.println("Cancelled");
	}

	private ChoiceDialogManualTest() {
		return;
	}
}