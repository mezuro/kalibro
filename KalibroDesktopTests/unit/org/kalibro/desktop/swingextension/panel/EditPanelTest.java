package org.kalibro.desktop.swingextension.panel;

import org.junit.Test;
import org.kalibro.Language;
import org.kalibro.tests.UnitTest;

public class EditPanelTest extends UnitTest {

	@Test
	public void shouldSet() {
		new LanguagePanelStub().set(Language.C);
	}
}