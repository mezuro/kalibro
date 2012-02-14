package org.kalibro.desktop.configuration;

import org.kalibro.core.model.RangeFixtures;
import org.kalibro.core.model.RangeLabel;
import org.kalibro.desktop.ComponentWrapperDialog;

public class RangePanelManualTest {

	public static void main(String[] args) {
		RangePanel panel = new RangePanel();
		panel.show(RangeFixtures.amlocRange(RangeLabel.WARNING));
		new ComponentWrapperDialog("RangePanel", panel).setVisible(true);
	}
}