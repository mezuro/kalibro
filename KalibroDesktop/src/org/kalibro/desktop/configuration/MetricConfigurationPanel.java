package org.kalibro.desktop.configuration;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import org.kalibro.core.model.MetricConfiguration;
import org.kalibro.core.model.Range;
import org.kalibro.core.model.enums.Statistic;
import org.kalibro.desktop.swingextension.Button;
import org.kalibro.desktop.swingextension.Label;
import org.kalibro.desktop.swingextension.field.ChoiceField;
import org.kalibro.desktop.swingextension.field.DoubleField;
import org.kalibro.desktop.swingextension.field.StringField;
import org.kalibro.desktop.swingextension.list.*;
import org.kalibro.desktop.swingextension.panel.EditPanel;
import org.kalibro.desktop.swingextension.panel.GridBagPanelBuilder;

public class MetricConfigurationPanel extends EditPanel<MetricConfiguration> {

	private MetricPanel metricPanel;
	private StringField codeField;
	private DoubleField weightField;
	private ChoiceField<Statistic> aggregationFormField;
	private TablePanel<Range> rangesPanel;

	private Button cancelButton, okButton;

	public MetricConfigurationPanel() {
		super("metricConfiguration");
	}

	@Override
	protected void createComponents() {
		metricPanel = new MetricPanel();
		metricPanel.setBorder(new TitledBorder("Metric"));
		codeField = new StringField("code", 10);
		aggregationFormField = new ChoiceField<Statistic>("aggregationForm", Statistic.values());
		weightField = new DoubleField("weight");
		createRangesPanel();
		cancelButton = new Button("cancel", "Cancel");
		okButton = new Button("ok", "Ok");
	}

	private void createRangesPanel() {
		ReflectionTableModel<Range> model = new ReflectionTableModel<Range>(Range.class);
		model.addColumn(new ReflectionColumn("beginning", 8));
		model.addColumn(new ReflectionColumn("end", 8));
		model.addColumn(new ReflectionColumn("label", 20, new RangeFieldRenderer()));
		model.addColumn(new ReflectionColumn("grade", 8, new RangeFieldRenderer()));
		Table<Range> rangesTable = new Table<Range>("ranges", model, 5);
		rangesPanel = new TablePanel<Range>(rangesTable);
		rangesPanel.setBorder(new TitledBorder("Ranges"));
	}

	@Override
	protected void buildPanel() {
		setLayout(new BorderLayout());
		add(mainPanel(), BorderLayout.CENTER);
		add(buttonsPanel(), BorderLayout.SOUTH);
	}

	private JPanel mainPanel() {
		GridBagPanelBuilder builder = new GridBagPanelBuilder();
		builder.add(metricPanel, 6);
		builder.newLine();
		builder.add(configurationPanel(), 1.0);
		builder.newLine();
		builder.add(rangesPanel, 6);
		return builder.getPanel();
	}

	private JPanel configurationPanel() {
		GridBagPanelBuilder builder = new GridBagPanelBuilder(new JPanel());
		builder.add(new Label("Code:"));
		builder.add(codeField, 3);
		builder.newLine();
		builder.addSimpleLine(new Label("Aggregation Form:"), aggregationFormField, new Label("Weight:"), weightField);
		return builder.getPanel();
	}

	private Component buttonsPanel() {
		JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		buttonsPanel.add(cancelButton);
		buttonsPanel.add(okButton);
		return buttonsPanel;
	}

	@Override
	public void show(MetricConfiguration configuration) {
		metricPanel.show(configuration.getMetric());
		codeField.set(configuration.getCode());
		weightField.set(configuration.getWeight());
		aggregationFormField.set(configuration.getAggregationForm());
		rangesPanel.show(configuration.getRanges());
	}

	@Override
	public MetricConfiguration get() {
		MetricConfiguration configuration = new MetricConfiguration(metricPanel.get());
		configuration.setCode(codeField.get());
		configuration.setWeight(weightField.get());
		configuration.setAggregationForm(aggregationFormField.get());
		for (Range range : rangesPanel.get())
			configuration.addRange(range);
		return configuration;
	}

	public void addRangesPanelListener(TablePanelListener<Range> listener) {
		rangesPanel.addTablePanelListener(listener);
	}

	public void addButtonListener(ActionListener listener) {
		cancelButton.addActionListener(listener);
		okButton.addActionListener(listener);
	}
}