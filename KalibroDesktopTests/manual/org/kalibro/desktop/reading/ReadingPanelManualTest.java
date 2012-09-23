package org.kalibro.desktop.reading;

import org.kalibro.Reading;
import org.kalibro.desktop.ComponentWrapperDialog;
import org.kalibro.tests.UnitTest;

public final class ReadingPanelManualTest extends ReadingPanel {

	public static void main(String[] args) {
		new ComponentWrapperDialog("ReadingPanel", new ReadingPanelManualTest()).setVisible(true);
	}

	private ReadingPanelManualTest() {
		super(UnitTest.loadFixture("excellent", Reading.class));
	}
}