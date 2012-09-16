package org.kalibro.desktop.reading;

import org.kalibro.Reading;
import org.kalibro.TestCase;
import org.kalibro.desktop.ComponentWrapperDialog;

public final class ReadingPanelManualTest extends ReadingPanel {

	public static void main(String[] args) {
		new ComponentWrapperDialog("ReadingPanel", new ReadingPanelManualTest()).setVisible(true);
	}

	private ReadingPanelManualTest() {
		super(TestCase.loadFixture("excellent", Reading.class));
	}
}