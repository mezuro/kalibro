package org.kalibro.desktop.reading;

import org.kalibro.ReadingGroup;
import org.kalibro.desktop.CrudController;

public class ReadingGroupController extends CrudController<ReadingGroup> {

	@Override
	protected Class<ReadingGroup> entityClass() {
		return ReadingGroup.class;
	}

	@Override
	protected ReadingGroupPanel panelFor(ReadingGroup group) {
		ReadingGroupPanel panel = new ReadingGroupPanel();
		panel.set(group);
		return panel;
	}
}