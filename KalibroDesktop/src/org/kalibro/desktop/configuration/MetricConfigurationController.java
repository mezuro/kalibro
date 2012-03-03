package org.kalibro.desktop.configuration;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.kalibro.core.model.Configuration;
import org.kalibro.core.model.MetricConfiguration;
import org.kalibro.core.model.Range;
import org.kalibro.desktop.swingextension.Button;
import org.kalibro.desktop.swingextension.dialog.ErrorDialog;
import org.kalibro.desktop.swingextension.list.TablePanelListener;
import org.kalibro.desktop.swingextension.panel.CardStackPanel;

public class MetricConfigurationController implements ActionListener, TablePanelListener<Range> {

	private Configuration configuration;
	private MetricConfiguration metricConfiguration;

	private CardStackPanel cardStack;
	private AddMetricDialog addDialog;
	private MetricConfigurationPanel panel;

	public MetricConfigurationController(Configuration configuration, CardStackPanel cardStack) {
		this.cardStack = cardStack;
		this.configuration = configuration;
		panel = new MetricConfigurationPanel();
		panel.addRangesPanelListener(this);
		panel.addButtonListener(this);
	}

	public void addMetricConfiguration() {
		addDialog = new AddMetricDialog();
		addDialog.addOkListener(new AddMetricListener());
		addDialog.setVisible(true);
	}

	public void edit(MetricConfiguration theMetricConfiguration) {
		metricConfiguration = theMetricConfiguration;
		showMetricConfiguration();
	}

	private void showMetricConfiguration() {
		panel.show(metricConfiguration);
		cardStack.push(panel);
	}

	@Override
	public void add() {
		new RangeController(metricConfiguration).addRange();
	}

	@Override
	public void edit(Range range) {
		new RangeController(metricConfiguration).editRange(range);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		boolean ok = ((Button) event.getSource()).getName().equals("ok");
		boolean editing = configuration.removeMetric(metricConfiguration.getMetric());
		if (ok)
			confirm(editing);
		else
			cancel(editing);
	}

	private void confirm(boolean editing) {
		try {
			configuration.addMetricConfiguration(panel.get());
			cardStack.pop();
		} catch (Exception exception) {
			putOldMetricConfigurationBack(editing);
			new ErrorDialog(panel).show(exception);
		}
	}

	private void cancel(boolean editing) {
		putOldMetricConfigurationBack(editing);
		cardStack.pop();
	}

	private void putOldMetricConfigurationBack(boolean editing) {
		if (editing)
			configuration.addMetricConfiguration(metricConfiguration);
	}

	private class AddMetricListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent event) {
			try {
				metricConfiguration = new MetricConfiguration(addDialog.getMetric());
				configuration.addMetricConfiguration(metricConfiguration);
				showMetricConfiguration();
				addDialog.dispose();
			} catch (Exception exception) {
				new ErrorDialog(addDialog).show(exception);
			}
		}
	}
}