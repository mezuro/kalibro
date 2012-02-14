package org.kalibro.desktop.old.model;

import static org.kalibro.desktop.old.model.ConfiguredResultTableModel.*;

import java.awt.Component;
import java.text.DecimalFormat;
import java.util.Arrays;

import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import org.kalibro.core.model.MetricConfiguration;
import org.kalibro.core.model.Range;
import org.kalibro.desktop.swingextension.renderer.DefaultRenderer;

public class ConfiguredResultTableCellRenderer implements TableCellRenderer {

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
		int row, int column) {

		Component component = new DefaultRenderer()
			.getTableCellRendererComponent(table, value, isSelected, false, 0, 0);
		Range range = range(table, row);
		if (! Arrays.asList(RANGE_NAME, GRADE, RESULT).contains(column) || range == null)
			return component;

		if (column == RANGE_NAME || column == GRADE)
			component.setBackground(range.getColor());
		else if (column == RESULT && range.isFinite())
			component = progressBar(range.getBeginning(), (Double) value, range.getEnd());
		return component;
	}

	private Range range(JTable table, int row) {
		MetricConfiguration configuredMetric = (MetricConfiguration) table.getValueAt(row, METRIC);
		Double value = (Double) table.getValueAt(row, RESULT);
		return (configuredMetric == null) ? null : configuredMetric.hasRangeFor(value) ? configuredMetric
			.getRangeFor(value) : null;
	}

	private static JProgressBar progressBar(Double beginning, Double result, Double end) {
		JProgressBar progressBar = new JProgressBar(beginning.intValue(), end.intValue());
		progressBar.setValue(result.intValue());
		progressBar.setStringPainted(true);
		progressBar.setString(new DecimalFormat().format(result));
		return progressBar;
	}
}