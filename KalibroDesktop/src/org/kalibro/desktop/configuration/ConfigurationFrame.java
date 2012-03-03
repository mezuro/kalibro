package org.kalibro.desktop.configuration;

import javax.swing.JInternalFrame;

import org.kalibro.core.model.Configuration;
import org.kalibro.core.model.MetricConfiguration;
import org.kalibro.desktop.swingextension.icon.Icon;
import org.kalibro.desktop.swingextension.list.TablePanelListener;
import org.kalibro.desktop.swingextension.panel.CardStackPanel;

public class ConfigurationFrame extends JInternalFrame implements TablePanelListener<MetricConfiguration> {

	private ConfigurationPanel configurationPanel;
	private CardStackPanel cardStack;

	public ConfigurationFrame(Configuration configuration) {
		super(configuration.getName(), true, true, true, true);
		new Icon(Icon.KALIBRO).replaceIconOf(this);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		buildContentPane(configuration);
		setName("configuration");
		setVisible(true);

	}

	private void buildContentPane(Configuration configuration) {
		configurationPanel = new ConfigurationPanel();
		configurationPanel.show(configuration);
		configurationPanel.addMetricConfigurationsPanelListener(this);
		cardStack = new CardStackPanel();
		cardStack.push(configurationPanel);
		setContentPane(cardStack);
		pack();
	}

	@Override
	public void add() {
		Configuration configuration = configurationPanel.get();
		createController(configuration).add();
		configurationPanel.show(configuration);
	}

	@Override
	public void edit(MetricConfiguration metricConfiguration) {
		Configuration configuration = configurationPanel.get();
		createController(configuration).edit(metricConfiguration);
		configurationPanel.show(configuration);
	}

	private MetricConfigurationController createController(Configuration configuration) {
		return new MetricConfigurationController(configuration, cardStack);
	}
}