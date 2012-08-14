package org.kalibro.desktop.swingextension.list;

import org.kalibro.core.model.Range;
import org.kalibro.desktop.ComponentWrapperDialog;

public final class TablePanelManualTest extends TablePanel<Range> implements TablePanelListener<Range> {

	public static void main(String[] args) {
		new ComponentWrapperDialog("TablePanel<Range>", new TablePanelManualTest()).setVisible(true);
	}

	private TablePanelManualTest() {
		super(new TableManualTest().getTable());
		addTablePanelListener(this);
	}

	@Override
	public void add() {
		System.out.println("Add");
	}

	@Override
	public void edit(Range range) {
		System.out.println("Edit " + range);
	}
}