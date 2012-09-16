package org.kalibro.desktop.reading;

import org.kalibro.Reading;
import org.kalibro.ReadingGroup;
import org.kalibro.TestCase;
import org.kalibro.desktop.ComponentWrapperDialog;
import org.kalibro.desktop.swingextension.list.TablePanelListener;

public final class ReadingGroupPanelManualTest implements TablePanelListener<Reading> {

	public static void main(String[] args) {
		new ReadingGroupPanelManualTest();
	}

	private ReadingGroupPanelManualTest() {
		ReadingGroup group = TestCase.loadFixture("scholar", ReadingGroup.class);
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