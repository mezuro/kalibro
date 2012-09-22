package org.kalibro.core.model.enums;

import static org.kalibro.core.model.enums.Language.CPP;

import org.kalibro.EnumerationTest;

public class LanguageTest extends EnumerationTest<Language> {

	@Override
	protected Class<Language> enumerationClass() {
		return Language.class;
	}

	@Override
	protected String expectedText(Language language) {
		return (language == CPP) ? "C++" : super.expectedText(language);
	}
}