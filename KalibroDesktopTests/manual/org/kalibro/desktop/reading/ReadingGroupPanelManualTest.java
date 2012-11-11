package org.kalibro.desktop.reading;

import static org.kalibro.tests.UnitTest.loadFixture;

import org.kalibro.ReadingGroup;
import org.kalibro.desktop.tests.ComponentWrapperDialog;

public final class ReadingGroupPanelManualTest {

	public static void main(String[] args) {
		new ReadingGroupPanelManualTest();
	}

	private ReadingGroupPanelManualTest() {
		ReadingGroupPanel panel = new ReadingGroupPanel();
		panel.set(loadFixture("scholar", ReadingGroup.class));
		new ComponentWrapperDialog("ReadingGroupPanel", panel).setVisible(true);
	}
}