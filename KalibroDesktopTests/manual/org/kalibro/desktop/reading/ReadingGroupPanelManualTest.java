package org.kalibro.desktop.reading;

import org.kalibro.ReadingGroup;
import org.kalibro.desktop.tests.ComponentWrapperDialog;
import org.kalibro.tests.UnitTest;

public final class ReadingGroupPanelManualTest {

	public static void main(String[] args) {
		new ReadingGroupPanelManualTest();
	}

	private ReadingGroupPanelManualTest() {
		ReadingGroup group = UnitTest.loadFixture("scholar", ReadingGroup.class);
		ReadingGroupPanel panel = new ReadingGroupPanel(group);
		new ComponentWrapperDialog("ReadingGroupPanel", panel).setVisible(true);
	}
}