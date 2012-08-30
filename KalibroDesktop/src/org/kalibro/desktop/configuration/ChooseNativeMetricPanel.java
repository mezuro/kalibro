package org.kalibro.desktop.configuration;

import java.awt.Component;

import javax.swing.border.TitledBorder;

import org.kalibro.core.Kalibro;
import org.kalibro.core.model.BaseTool;
import org.kalibro.core.model.NativeMetric;
import org.kalibro.desktop.swingextension.field.TextField;
import org.kalibro.desktop.swingextension.list.*;
import org.kalibro.desktop.swingextension.panel.AbstractPanel;
import org.kalibro.desktop.swingextension.panel.GridBagPanelBuilder;

public class ChooseNativeMetricPanel extends AbstractPanel<NativeMetric> {

	private List<String> baseToolList;
	private Table<NativeMetric> metricTable;
	private TextField descriptionField;

	public ChooseNativeMetricPanel() {
		super("chooseNativeMetric");
	}

	@Override
	protected void createComponents(Component... innerComponents) {
		createBaseToolList();
		createMetricTable();
		descriptionField = new TextField("description", 2, 20, null, true);
	}

	private void createBaseToolList() {
		baseToolList = new List<String>("baseTools", Kalibro.getBaseToolDao().getBaseToolNames(), 5);
		baseToolList.setBorder(new TitledBorder("Base tool"));
		baseToolList.addListListener(new BaseToolListListener());
	}

	private void createMetricTable() {
		ReflectionTableModel<NativeMetric> model = new ReflectionTableModel<NativeMetric>(NativeMetric.class);
		model.addColumn(new ReflectionColumn("scope", 7));
		model.addColumn(new ReflectionColumn("name", 25));
		model.addColumn(new ReflectionColumn("languages", 8));
		metricTable = new Table<NativeMetric>("supportedMetrics", model, 10);
		metricTable.setBorder(new TitledBorder("Metric"));
		metricTable.addListListener(new MetricTableListener());
	}

	@Override
	protected void buildPanel() {
		GridBagPanelBuilder builder = new GridBagPanelBuilder(this);
		builder.addSimpleLine(baseToolList, metricTable);
		builder.add(descriptionField, 2);
	}

	@Override
	public NativeMetric get() {
		return metricTable.getSelected();
	}

	public boolean hasSelectedMetric() {
		return metricTable.hasSelection();
	}

	public void addListListener(ListListener<NativeMetric> listener) {
		metricTable.addListListener(listener);
	}

	private class BaseToolListListener extends ListAdapter<String> {

		@Override
		public void selected(String baseToolName) {
			BaseTool baseTool = Kalibro.getBaseToolDao().getBaseTool(baseToolName);
			metricTable.setData(baseTool.getSupportedMetrics());
			descriptionField.set(baseTool.getDescription());
		}
	}

	private class MetricTableListener extends ListAdapter<NativeMetric> {

		@Override
		public void selected(NativeMetric metric) {
			descriptionField.set(metric.getDescription());
		}
	}
}