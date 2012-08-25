package org.kalibro.desktop.old.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.kalibro.core.model.Metric;
import org.kalibro.core.model.MetricResult;
import org.kalibro.core.model.ModuleResult;
import org.kalibro.core.model.Range;
import org.kalibro.core.model.enums.Statistic;
import org.kalibro.desktop.swingextension.list.TableModel;

public class ResultTableModel extends TableModel<MetricResult> {

	protected TableDefinition tableDefinition;

	public ResultTableModel() {
		tableDefinition = new TableDefinition(columnNames());
		tableDefinition.columnClasses(Metric.class, Double.class, Integer.class, Double.class);
		data = new ArrayList<MetricResult>();
	}

	private List<Object> columnNames() {
		List<Object> columnNames = new ArrayList<Object>();
		columnNames.addAll(Arrays.asList("ConfiguredMetric", "Value", "Subcomponents"));
		columnNames.addAll(Arrays.asList(Statistic.values()));
		return columnNames;
	}

	protected void refreshTableData() {
		tableDefinition.clearData();
		for (MetricResult result : data)
			tableDefinition.addRow(rowFor(result));
	}

	private List<Object> rowFor(MetricResult result) {
		List<Object> row = new ArrayList<Object>();
		row.addAll(Arrays.asList(result.getMetric(), result.getValue(), result.getDescendentResults().size()));
		for (Statistic statistic : Statistic.values())
			row.add(result.getStatistic(statistic));
		return row;
	}

	public void moduleResult(ModuleResult moduleResult) {
		data.clear();
		if (moduleResult != null) {
			data.addAll(moduleResult.getMetricResults());
			Collections.sort(data);
		}
		refreshTableData();
		fireTableDataChanged();
	}

	public MetricResult metricResult(int row) {
		if (data.isEmpty() || row < 0)
			return null;
		return data.get(row);
	}

	@SuppressWarnings("unused")
	public Range range(int row) {
		return null;
	}

	@Override
	public int getColumnCount() {
		return tableDefinition.columnCount();
	}

	@Override
	public String getColumnName(int column) {
		return tableDefinition.columnName(column);
	}

	@Override
	public Class<?> getColumnClass(int column) {
		return tableDefinition.columnClass(column);
	}

	@Override
	public int getRowCount() {
		return tableDefinition.rowCount();
	}

	@Override
	public Object getValueAt(int row, int column) {
		return tableDefinition.valueAt(row, column);
	}
}