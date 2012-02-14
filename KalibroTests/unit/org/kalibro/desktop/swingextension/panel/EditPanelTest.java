package org.kalibro.desktop.swingextension.panel;

import org.junit.Test;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.model.enums.Language;

public class EditPanelTest extends KalibroTestCase {

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldShow() {
		new LanguagePanelStub().show(Language.C);
	}
}