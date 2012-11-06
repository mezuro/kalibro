package org.kalibro.desktop.swingextension.panel;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.kalibro.Language;
import org.kalibro.tests.UnitTest;

public class EditPanelTest extends UnitTest {

	@Test
	public void shouldSetValue() {
		EditPanel<Language> panel = new LanguagePanel();
		panel.set(Language.C);
		assertEquals(Language.C, panel.get());
	}
}