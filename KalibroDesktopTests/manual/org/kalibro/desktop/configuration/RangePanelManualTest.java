package org.kalibro.desktop.configuration;

import static org.kalibro.RangeFixtures.*;
import static org.kalibro.RangeLabel.*;

import org.kalibro.desktop.ComponentWrapperDialog;

public final class RangePanelManualTest extends RangePanel {

	public static void main(String[] args) {
		new ComponentWrapperDialog("RangePanel", new RangePanelManualTest()).setVisible(true);
	}

	private RangePanelManualTest() {
		super();
		set(newRange("amloc", WARNING));
	}
}