package org.kalibro.desktop.swingextension.dialog;

import java.awt.Component;

import org.kalibro.core.model.enums.Language;
import org.kalibro.desktop.ComponentWrapperDialog;

public final class ChoiceDialogManualTest {

	public static void main(String[] args) {
		Component parent = new ComponentWrapperDialog("");
		ChoiceDialog<Language> dialog = new ChoiceDialog<Language>("Choose language", parent);
		System.out.println(dialog.choose("Please select the language:", Language.values()));
		System.exit(0);
	}

	private ChoiceDialogManualTest() {
		// Utility class
	}
}