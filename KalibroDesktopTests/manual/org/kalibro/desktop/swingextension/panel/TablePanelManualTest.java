package org.kalibro.desktop.swingextension.panel;

import static org.kalibro.tests.UnitTest.loadFixture;

import java.awt.Color;
import java.util.SortedSet;

import javax.swing.JPanel;

import org.kalibro.Range;
import org.kalibro.Reading;
import org.kalibro.ReadingGroup;
import org.kalibro.desktop.swingextension.table.ColumnRenderer;
import org.kalibro.desktop.swingextension.table.Table;
import org.kalibro.desktop.tests.ComponentWrapperDialog;

public final class TablePanelManualTest extends TablePanel<Range> implements TablePanelListener<Range> {

	public static void main(String[] args) {
		new ComponentWrapperDialog("TablePanel<Range>", new TablePanelManualTest()).setVisible(true);
	}

	private static Table<Range> createTable() {
		Table<Range> table = new Table<Range>("", 9, Range.class);
		table.addColumn("beginning").withWidth(8);
		table.addColumn("end").withWidth(8);
		table.addColumn("reading", "label").titled("Label").withWidth(15);
		table.addColumn("reading", "grade").titled("Grade").withWidth(8);
		table.addColumn("reading", "color").titled("Color").withWidth(5).renderedBy(new ColorRenderer());
		table.pack();
		return table;
	}

	private Double beginning;
	private SortedSet<Reading> readings;

	private TablePanelManualTest() {
		super(createTable());
		addTablePanelListener(this);
		beginning = 0.0;
		readings = loadFixture("scholar", ReadingGroup.class).getReadings();
	}

	@Override
	public void add() {
		if (readings.isEmpty())
			return;
		Reading reading = readings.last();
		Range range = new Range(beginning, beginning + 1.0);
		range.setReading(reading);
		beginning = range.getEnd();
		readings.remove(reading);
		table.add(range);
	}

	@Override
	public void edit(Range range) {
		Reading reading = range.getReading();
		reading.setGrade(reading.getGrade() + 0.1);
		repaint();
	}

	@Override
	public void remove(Range range) {
		readings.add(range.getReading());
	}

	private static class ColorRenderer extends ColumnRenderer {

		@Override
		public JPanel render(Object value, Object context) {
			JPanel panel = new JPanel();
			panel.setBackground((Color) value);
			return panel;
		}
	}
}