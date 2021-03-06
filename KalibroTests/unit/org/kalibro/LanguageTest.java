package org.kalibro;

import static org.kalibro.Language.CPP;

import org.kalibro.tests.EnumerationTest;

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