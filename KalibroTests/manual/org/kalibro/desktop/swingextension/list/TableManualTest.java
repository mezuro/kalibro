package org.kalibro.desktop.swingextension.list;

import static org.kalibro.core.model.MetricConfigurationFixtures.*;

import java.awt.Color;

import javax.swing.JPanel;

import org.kalibro.core.model.Range;
import org.kalibro.desktop.ComponentWrapperDialog;
import org.kalibro.desktop.swingextension.renderer.TableRenderer;

public class TableManualTest implements ListListener<Range> {

	public static void main(String[] args) {
		new ComponentWrapperDialog("Table<Range>", createTable()).setVisible(true);
		System.exit(0);
	}

	public static Table<Range> createTable() {
		Table<Range> table = new Table<Range>("", createModel(), 5);
		table.setData(configuration("amloc").getRanges());
		table.addListListener(new TableManualTest());
		return table;
	}

	private static TableModel<Range> createModel() {
		TableModel<Range> model = new ReflectionTableModel<Range>(Range.class);
		model.addColumn(new ReflectionColumn("beginning", 8));
		model.addColumn(new ReflectionColumn("end", 8));
		model.addColumn(new ReflectionColumn("label", 15));
		model.addColumn(new ReflectionColumn("grade", 8));
		model.addColumn(new ReflectionColumn("color", 5, new ColorRenderer()));
		return model;
	}

	@Override
	public void doubleClicked(Range range) {
		System.out.println("Double clicked " + range);
	}

	@Override
	public void selected(Range range) {
		System.out.println("Selected " + range);
	}

	@Override
	public void selectionCleared() {
		System.out.println("Selection cleared");
	}

	private static class ColorRenderer extends TableRenderer {

		@Override
		protected JPanel render(Object value, Object context) {
			JPanel panel = new JPanel();
			panel.setBackground((Color) value);
			return panel;
		}
	}
}