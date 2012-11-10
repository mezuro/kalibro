package org.kalibro.desktop.configuration;

import java.awt.Component;

import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import org.kalibro.MetricConfiguration;
import org.kalibro.Range;
import org.kalibro.Statistic;
import org.kalibro.desktop.swingextension.Label;
import org.kalibro.desktop.swingextension.field.ChoiceField;
import org.kalibro.desktop.swingextension.field.DoubleField;
import org.kalibro.desktop.swingextension.field.StringField;
import org.kalibro.desktop.swingextension.panel.EditPanel;
import org.kalibro.desktop.swingextension.panel.GridBagPanelBuilder;
import org.kalibro.desktop.swingextension.panel.TablePanel;
import org.kalibro.desktop.swingextension.panel.TablePanelController;
import org.kalibro.desktop.swingextension.table.Table;

public class MetricConfigurationPanel extends EditPanel<MetricConfiguration> implements
	TablePanelController<Range> {

	private MetricPanel metricPanel;
	private StringField codeField;
	private DoubleField weightField;
	private ChoiceField<Statistic> aggregationFormField;
	private TablePanel<Range> rangesPanel;

	public MetricConfigurationPanel() {
		super("metricConfiguration");
	}

	@Override
	protected void createComponents(Component... innerComponents) {
		metricPanel = new MetricPanel();
		metricPanel.setBorder(new TitledBorder("Metric"));
		codeField = new StringField("code", 10);
		aggregationFormField = new ChoiceField<Statistic>("aggregationForm", Statistic.values());
		weightField = new DoubleField("weight");
		createRangesPanel();
	}

	private void createRangesPanel() {
		Table<Range> table = new Table<Range>("ranges", 5, Range.class);
		table.addColumn("beginning").withWidth(8);
		table.addColumn("end").withWidth(8);
		table.addColumn("reading", "label").titled("Label").withWidth(20).renderedBy(new RangeFieldRenderer());
		table.addColumn("reading", "grade").titled("grade").withWidth(8).renderedBy(new RangeFieldRenderer());
		table.pack();
		rangesPanel = new TablePanel<Range>(this, table);
		rangesPanel.setBorder(new TitledBorder("Ranges"));
	}

	@Override
	protected void buildPanel() {
		GridBagPanelBuilder builder = new GridBagPanelBuilder(this);
		builder.add(metricPanel, 6);
		builder.newLine();
		builder.add(configurationPanel(), 1.0);
		builder.newLine();
		builder.add(rangesPanel, 6);
	}

	private JPanel configurationPanel() {
		GridBagPanelBuilder builder = new GridBagPanelBuilder(new JPanel());
		builder.add(new Label("Code:"));
		builder.add(codeField, 3);
		builder.newLine();
		builder.addSimpleLine(new Label("Aggregation Form:"), aggregationFormField, new Label("Weight:"), weightField);
		return builder.getPanel();
	}

	@Override
	public MetricConfiguration get() {
		MetricConfiguration configuration = new MetricConfiguration();
		configuration.setCode(codeField.get());
		configuration.setWeight(weightField.get());
		configuration.setAggregationForm(aggregationFormField.get());
		for (Range range : rangesPanel.get())
			configuration.addRange(range);
		return configuration;
	}

	@Override
	public void set(MetricConfiguration configuration) {
		metricPanel.set(configuration.getMetric());
		codeField.set(configuration.getCode());
		weightField.set(configuration.getWeight());
		aggregationFormField.set(configuration.getAggregationForm());
		rangesPanel.set(configuration.getRanges());
	}

	@Override
	public Range add() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Range edit(Range element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void remove(Range element) {
		// TODO Auto-generated method stub

	}
}