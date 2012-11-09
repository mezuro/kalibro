package org.kalibro.desktop.reading;

import org.kalibro.Reading;
import org.kalibro.ReadingGroup;
import org.kalibro.desktop.swingextension.table.TablePanelListener;
import org.kalibro.desktop.tests.ComponentWrapperDialog;
import org.kalibro.tests.UnitTest;

public final class ReadingGroupPanelManualTest implements TablePanelListener<Reading> {

	public static void main(String[] args) {
		new ReadingGroupPanelManualTest();
	}

	private ReadingGroupPanelManualTest() {
		ReadingGroup group = UnitTest.loadFixture("scholar", ReadingGroup.class);
		ReadingGroupPanel panel = new ReadingGroupPanel(group);
		panel.addReadingsListener(this);
		new ComponentWrapperDialog("ReadingGroupPanel", panel).setVisible(true);
	}

	@Override
	public void add() {
		System.out.println("Add reading");
	}

	@Override
	public void edit(Reading reading) {
		System.out.println("Edit " + reading);
	}
}