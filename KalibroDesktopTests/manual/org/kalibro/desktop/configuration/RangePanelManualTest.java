package org.kalibro.desktop.configuration;

import static org.kalibro.tests.UnitTest.loadFixture;

import org.kalibro.Range;
import org.kalibro.desktop.tests.ComponentWrapperDialog;

@Deprecated
public final class RangePanelManualTest extends RangePanel {

	public static void main(String[] args) {
		new ComponentWrapperDialog("RangePanel", new RangePanelManualTest()).setVisible(true);
	}

	private RangePanelManualTest() {
		super();
		set(loadFixture("lcom4-bad", Range.class));
	}
}