package org.kalibro.desktop.configuration;

import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;

import javax.swing.JInternalFrame;

import org.kalibro.core.model.Configuration;
import org.kalibro.core.model.MetricConfiguration;
import org.kalibro.desktop.swingextension.icon.Icon;
import org.kalibro.desktop.swingextension.list.TablePanelListener;
import org.kalibro.desktop.swingextension.panel.CardStackPanel;

public class ConfigurationFrame extends JInternalFrame implements TablePanelListener<MetricConfiguration> {

	private ConfigurationPanel configurationPanel;
	private CardStackPanel cardStack;

	private Configuration configuration;

	public ConfigurationFrame(Configuration configuration) {
		super(configuration.getName() + " - Configuration", true, true, true, true);
		new Icon(Icon.KALIBRO).replaceIconOf(this);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setName("configuration");

		this.configuration = configuration;
		buildContentPane();
		setVisible(true);
	}

	private void buildContentPane() {
		configurationPanel = new ConfigurationPanel();
		configurationPanel.set(configuration);
		configurationPanel.addMetricConfigurationsListener(this);
		cardStack = new CardStackPanel();
		cardStack.push(configurationPanel);
		cardStack.addContainerListener(new CardStackListener());
		setContentPane(cardStack);
		pack();
	}

	@Override
	public void add() {
		configuration = configurationPanel.get();
		new MetricConfigurationController(configuration, cardStack).add();
	}

	@Override
	public void edit(MetricConfiguration metricConfiguration) {
		configuration = configurationPanel.get();
		new MetricConfigurationController(configuration, cardStack).edit(metricConfiguration);
	}

	private class CardStackListener extends ContainerAdapter {

		@Override
		public void componentRemoved(ContainerEvent event) {
			configurationPanel.set(configuration);
		}
	}
}