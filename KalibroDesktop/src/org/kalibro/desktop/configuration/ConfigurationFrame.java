package org.kalibro.desktop.configuration;

import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;

import org.kalibro.core.model.Configuration;
import org.kalibro.core.model.MetricConfiguration;
import org.kalibro.desktop.swingextension.InternalFrame;
import org.kalibro.desktop.swingextension.list.TablePanelListener;
import org.kalibro.desktop.swingextension.panel.CardStackPanel;

public class ConfigurationFrame extends InternalFrame<Configuration> implements ContainerListener,
	TablePanelListener<MetricConfiguration> {

	private ConfigurationPanel configurationPanel;
	private CardStackPanel cardStack;

	public ConfigurationFrame(Configuration configuration) {
		super("configuration", configuration.getName() + " - Configuration", configuration);
	}

	@Override
	protected CardStackPanel buildContentPane() {
		configurationPanel = new ConfigurationPanel();
		configurationPanel.set(entity);
		configurationPanel.addMetricConfigurationsListener(this);
		cardStack = new CardStackPanel();
		cardStack.push(configurationPanel);
		cardStack.addContainerListener(this);
		return cardStack;
	}

	public Configuration getConfiguration() {
		entity = configurationPanel.get();
		return entity;
	}

	@Override
	public void add() {
		entity = configurationPanel.get();
		new MetricConfigurationController(entity, cardStack).addMetricConfiguration();
	}

	@Override
	public void edit(MetricConfiguration metricConfiguration) {
		entity = configurationPanel.get();
		new MetricConfigurationController(entity, cardStack).edit(metricConfiguration);
	}

	@Override
	public void componentAdded(ContainerEvent event) {
		adjustSize();
	}

	@Override
	public void componentRemoved(ContainerEvent event) {
		configurationPanel.set(entity);
		adjustSize();
	}
}