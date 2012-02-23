package org.kalibro.desktop.swingextension.list;

import org.kalibro.core.model.Range;
import org.kalibro.desktop.ComponentWrapperDialog;

public class TablePanelManualTest implements TablePanelListener<Range> {

	public static void main(String[] args) {
		Table<Range> table = TableManualTest.createTable();
		TablePanel<Range> panel = new TablePanel<Range>(table);
		panel.addTablePanelListener(new TablePanelManualTest());
		new ComponentWrapperDialog("TablePanel<Range>", panel).setVisible(true);
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