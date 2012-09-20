package org.kalibro.desktop.configuration;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import org.kalibro.Configuration;
import org.kalibro.core.model.MetricConfiguration;
import org.kalibro.desktop.swingextension.Label;
import org.kalibro.desktop.swingextension.field.TextField;
import org.kalibro.desktop.swingextension.field.UneditableField;
import org.kalibro.desktop.swingextension.list.*;
import org.kalibro.desktop.swingextension.panel.EditPanel;
import org.kalibro.desktop.swingextension.panel.GridBagPanelBuilder;

public class ConfigurationPanel extends EditPanel<Configuration> {

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
		ReflectionTableModel<MetricConfiguration> model;
		model = new ReflectionTableModel<MetricConfiguration>(MetricConfiguration.class);
		model.addColumn(new ReflectionColumn("code", 6));
		model.addColumn(new ReflectionColumn("weight", 5));
		model.addColumn(new ReflectionColumn("metric.compound", 7));
		model.addColumn(new ReflectionColumn("metric.scope", 7));
		model.addColumn(new ReflectionColumn("metric", 25));
		Table<MetricConfiguration> metricsTable = new Table<MetricConfiguration>("metricConfigurations", model, 15);
		metricConfigurationsPanel = new TablePanel<MetricConfiguration>(metricsTable);
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

	public void addMetricConfigurationsListener(TablePanelListener<MetricConfiguration> listener) {
		metricConfigurationsPanel.addTablePanelListener(listener);
	}
}