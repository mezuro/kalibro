package org.kalibro.desktop.swingextension.list;

import static org.kalibro.MetricConfigurationFixtures.*;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JPanel;

import org.kalibro.Range;
import org.kalibro.desktop.ComponentWrapperDialog;
import org.kalibro.desktop.swingextension.renderer.TableRenderer;

public final class TableManualTest extends JPanel implements ListListener<Range> {

	public static void main(String[] args) {
		new ComponentWrapperDialog("Table<Range>", new TableManualTest()).setVisible(true);
	}

	private Table<Range> table;

	protected TableManualTest() {
		super(new GridLayout());
		table = new Table<Range>("", createModel(), 5);
		table.setData(metricConfiguration("amloc").getRanges());
		table.addListListener(this);
		add(table);
	}

	private TableModel<Range> createModel() {
		TableModel<Range> model = new ReflectionTableModel<Range>(Range.class);
		model.addColumn(new ReflectionColumn("beginning", 8));
		model.addColumn(new ReflectionColumn("end", 8));
		model.addColumn(new ReflectionColumn("label", 15));
		model.addColumn(new ReflectionColumn("grade", 8));
		model.addColumn(new ReflectionColumn("color", 5, new ColorRenderer()));
		return model;
	}

	protected Table<Range> getTable() {
		return table;
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

	private class ColorRenderer extends TableRenderer {

		@Override
		protected JPanel render(Object value, Object context) {
			JPanel panel = new JPanel();
			panel.setBackground((Color) value);
			return panel;
		}
	}
}