package org.kalibro.desktop.reading;

import org.kalibro.ReadingGroup;
import org.kalibro.desktop.CrudController;
import org.kalibro.desktop.KalibroFrame;

public class ReadingGroupController extends CrudController<ReadingGroup> {

	public ReadingGroupController(KalibroFrame kalibroFrame) {
		super(kalibroFrame);
	}

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