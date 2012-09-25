package org.kalibro.desktop.configuration;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.kalibro.*;
import org.kalibro.desktop.swingextension.Button;
import org.kalibro.desktop.swingextension.dialog.ErrorDialog;
import org.kalibro.desktop.swingextension.list.TablePanelListener;
import org.kalibro.desktop.swingextension.panel.CardStackPanel;
import org.kalibro.desktop.swingextension.panel.ConfirmPanel;

public class MetricConfigurationController implements ActionListener, TablePanelListener<Range> {

	private MetricConfiguration metricConfiguration;
	private Configuration configuration;

	private ConfirmPanel<MetricConfiguration> panel;
	private AddMetricDialog addDialog;
	private CardStackPanel cardStack;

	public MetricConfigurationController(Configuration configuration, CardStackPanel cardStack) {
		this.configuration = configuration;
		this.cardStack = cardStack;
		MetricConfigurationPanel metricPanel = new MetricConfigurationPanel();
		metricPanel.addRangesListener(this);
		panel = new ConfirmPanel<MetricConfiguration>(metricPanel);
		panel.addCancelListener(this);
		panel.addOkListener(this);
	}

	public void addMetricConfiguration() {
		addDialog = new AddMetricDialog();
		addDialog.addOkListener(this);
		addDialog.setVisible(true);
	}

	public void edit(MetricConfiguration theMetricConfiguration) {
		metricConfiguration = theMetricConfiguration;
		showMetricConfiguration();
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		Button source = (Button) event.getSource();
		if (addDialog != null && addDialog.isAncestorOf(source))
			addMetric();
		else if (panel.isAncestorOf(source))
			panelButtonClicked(source.getName());
	}

	private void addMetric() {
		try {
			metricConfiguration = new MetricConfiguration(addDialog.getMetric());
			configuration.addMetricConfiguration(metricConfiguration);
			showMetricConfiguration();
			addDialog.dispose();
		} catch (KalibroException exception) {
			new ErrorDialog(addDialog).show(exception);
		}
	}

	private void showMetricConfiguration() {
		panel.set(metricConfiguration);
		cardStack.push(panel);
	}

	private void panelButtonClicked(String buttonName) {
		if (buttonName.equals("cancel"))
			cardStack.pop();
		else if (buttonName.equals("ok"))
			confirmEdit();
	}

	private void confirmEdit() {
		try {
			Metric metric = metricConfiguration.getMetric();
			configuration.replaceMetricConfiguration(metric.getName(), panel.get());
			cardStack.pop();
		} catch (KalibroException exception) {
			new ErrorDialog(panel).show(exception);
		}
	}

	@Override
	public void add() {
		metricConfiguration = panel.get();
		new RangeController(metricConfiguration).addRange();
		panel.set(metricConfiguration);
	}

	@Override
	public void edit(Range range) {
		metricConfiguration = panel.get();
		new RangeController(metricConfiguration).editRange(range);
		panel.set(metricConfiguration);
	}
}