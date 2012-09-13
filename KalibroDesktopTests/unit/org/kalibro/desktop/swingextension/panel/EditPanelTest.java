package org.kalibro.desktop.swingextension.panel;

import org.junit.Test;
import org.kalibro.TestCase;
import org.kalibro.core.model.enums.Language;

public class EditPanelTest extends TestCase {

	@Test
	public void shouldSet() {
		new LanguagePanelStub().set(Language.C);
	}
}