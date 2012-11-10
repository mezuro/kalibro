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

public final class TablePanelManualTest extends ColumnRenderer implements TablePanelController<Range> {

	public static void main(String[] args) {
		TablePanelManualTest test = new TablePanelManualTest();

		Table<Range> table = new Table<Range>("", 9, Range.class);
		table.addColumn("beginning").withWidth(8);
		table.addColumn("end").withWidth(8);
		table.addColumn("reading", "label").titled("Label").withWidth(15);
		table.addColumn("reading", "grade").titled("Grade").withWidth(8);
		table.addColumn("reading", "color").titled("Color").withWidth(5).renderedBy(test);
		table.pack();

		new ComponentWrapperDialog("TablePanel<Range>", new TablePanel<Range>(test, table)).setVisible(true);
	}

	private Double beginning;
	private SortedSet<Reading> readings;

	private TablePanelManualTest() {
		beginning = 0.0;
		readings = loadFixture("scholar", ReadingGroup.class).getReadings();
	}

	@Override
	protected JPanel render(Object value, Object context) {
		JPanel panel = new JPanel();
		panel.setBackground((Color) value);
		return panel;
	}

	@Override
	public Range add() {
		if (readings.isEmpty())
			return null;
		Reading reading = readings.last();
		Range range = new Range(beginning, beginning + 1.0);
		range.setReading(reading);
		beginning = range.getEnd();
		readings.remove(reading);
		return range;
	}

	@Override
	public Range edit(Range range) {
		Reading reading = range.getReading();
		reading.setGrade(reading.getGrade() + 0.1);
		return range;
	}

	@Override
	public void remove(Range range) {
		readings.add(range.getReading());
	}
}