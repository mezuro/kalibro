package org.kalibro.desktop.swingextension;

import java.awt.Container;

import org.kalibro.core.model.Range;
import org.kalibro.desktop.configuration.RangePanel;

class RangeFrame extends InternalFrame<Range> {

	public RangeFrame(Range range) {
		super("range", "Range " + range, range);
	}

	@Override
	protected Container buildContentPane() {
		RangePanel rangePanel = new RangePanel();
		rangePanel.set(entity);
		return rangePanel;
	}
}