package org.kalibro.desktop.swingextension.dialog;

import org.kalibro.Language;

public final class EditDialogManualTest extends LanguageDialog implements EditDialogListener<Language> {

	public static void main(String[] args) {
		new EditDialogManualTest().setVisible(true);
	}

	private EditDialogManualTest() {
		super(null);
		addListener(this);
	}

	@Override
	public boolean dialogConfirm(Language language) {
		System.out.println(language);
		if (language == Language.PYTHON)
			throw new RuntimeException("Cannot accept Python");
		return language.name().startsWith("C");
	}
}