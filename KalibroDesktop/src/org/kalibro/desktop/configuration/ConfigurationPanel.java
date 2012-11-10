package org.kalibro.desktop.configuration;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import org.kalibro.Configuration;
import org.kalibro.MetricConfiguration;
import org.kalibro.desktop.swingextension.Label;
import org.kalibro.desktop.swingextension.field.TextField;
import org.kalibro.desktop.swingextension.field.UneditableField;
import org.kalibro.desktop.swingextension.panel.EditPanel;
import org.kalibro.desktop.swingextension.panel.GridBagPanelBuilder;
import org.kalibro.desktop.swingextension.panel.TablePanel;
import org.kalibro.desktop.swingextension.panel.TablePanelController;
import org.kalibro.desktop.swingextension.table.Table;

public class ConfigurationPanel extends EditPanel<Configuration> implements TablePanelController<MetricConfiguration> {

	private UneditableField<String> nameField;
	private TextField descriptionField;
	private TablePanel<MetricConfiguration> metricConfigurationsPanel;

	public ConfigurationPanel() {
		super("configuration");
	}

	@Override
	protected void createComponents(Component... innerComponents) {
		nameField = new UneditableField<String>("name");
		descriptionField = new TextField("description", 4, 30);
		createMetricsPanel();
	}

	private void createMetricsPanel() {
		Table<MetricConfiguration> table =
			new Table<MetricConfiguration>("metricConfigurations", 15, MetricConfiguration.class);
		table.addColumn("code").withWidth(6);
		table.addColumn("weight").withWidth(5);
		table.addColumn("metric", "name").withWidth(25);
		table.addColumn("metric", "compound").titled("Compound").withWidth(7);
		table.addColumn("metric", "scope").titled("Scope").withWidth(7);
		table.pack();
		metricConfigurationsPanel = new TablePanel<MetricConfiguration>(this, table);
		metricConfigurationsPanel.setBorder(new TitledBorder("Metric configurations"));
	}

	@Override
	protected void buildPanel() {
		setLayout(new BorderLayout());
		add(buildConfigurationPanel(), BorderLayout.NORTH);
		add(metricConfigurationsPanel, BorderLayout.CENTER);
	}

	private JPanel buildConfigurationPanel() {
		GridBagPanelBuilder builder = new GridBagPanelBuilder();
		builder.addSimpleLine(new Label("Name:"), nameField);
		builder.addSimpleLine(new Label("Description:"), descriptionField);
		return builder.getPanel();
	}

	@Override
	public Configuration get() {
		Configuration configuration = new Configuration();
		configuration.setName(nameField.get());
		configuration.setDescription(descriptionField.get());
		for (MetricConfiguration metric : metricConfigurationsPanel.get())
			configuration.addMetricConfiguration(metric);
		return configuration;
	}

	@Override
	public void set(Configuration configuration) {
		nameField.set(configuration.getName());
		descriptionField.set(configuration.getDescription());
		metricConfigurationsPanel.set(configuration.getMetricConfigurations());
	}

	@Override
	public MetricConfiguration add() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MetricConfiguration edit(MetricConfiguration element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void remove(MetricConfiguration element) {
		// TODO Auto-generated method stub

	}
}