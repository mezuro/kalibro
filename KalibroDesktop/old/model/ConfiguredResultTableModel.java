package org.kalibro.desktop.old.model;

import java.util.Collections;

import org.kalibro.core.model.MetricConfiguration;
import org.kalibro.core.model.MetricResult;
import org.kalibro.core.model.ModuleResult;
import org.kalibro.core.model.Range;
import org.kalibro.core.model.enums.Statistic;

public class ConfiguredResultTableModel extends ResultTableModel {

	public static final int METRIC = 0;
	public static final int CATEGORY = 1;
	public static final int WEIGHT = 2;
	public static final int GRADE = 3;
	public static final int RANGE_NAME = 4;
	public static final int BEGINNING = 5;
	public static final int RESULT = 6;
	public static final int END = 7;

	public ConfiguredResultTableModel() {
		super();
		tableDefinition = new TableDefinition("Metric", "Category", "Weight", "Grade", "Range name",
			"Beginning", "Result", "End");
		tableDefinition.columnClasses(MetricConfiguration.class, Statistic.class, Double.class, Double.class,
			String.class, Double.class);
	}

	private void addRow(MetricResult metricResult) {
		MetricConfiguration configuredMetric = new MetricConfiguration(metricResult.getMetric());
		Statistic category = configuredMetric.getAggregationForm();
		Double weight = configuredMetric.getWeight();
		Double result = metricResult.getValue();
		Range range = (configuredMetric.hasRangeFor(result)) ? configuredMetric.getRangeFor(result) : null;
		Double grade = (range == null) ? null : range.getGrade();
		String rangeName = (range == null) ? null : range.getLabel();
		Double beginning = (range == null) ? null : range.getBeginning();
		Double end = (range == null) ? null : range.getEnd();
		tableDefinition.addRow(configuredMetric, category, weight, grade, rangeName, beginning, result, end);
	}

	@Override
	public void moduleResult(ModuleResult moduleResult) {
		data.clear();
		if (moduleResult != null) {
			data.addAll(moduleResult.getMetricResults());
			Collections.sort(data);
		}
		refreshTableData();
		fireTableDataChanged();
	}

	@Override
	protected void refreshTableData() {
		tableDefinition.clearData();
		for (MetricResult result : data)
			addRow(result);
	}

	@Override
	public Range range(int row) {
		MetricResult metricResult = metricResult(row);
		if (metricResult == null)
			return null;
		MetricConfiguration configuredMetric = new MetricConfiguration(metricResult.getMetric());
		Double result = metricResult.getValue();
		if (configuredMetric.hasRangeFor(result))
			return configuredMetric.getRangeFor(result);
		return null;
	}
}