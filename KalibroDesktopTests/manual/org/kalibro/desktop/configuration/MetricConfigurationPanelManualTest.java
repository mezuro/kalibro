package org.kalibro.desktop.configuration;

import static org.kalibro.MetricConfigurationFixtures.*;
import static org.kalibro.MetricFixtures.*;

import org.kalibro.MetricConfiguration;
import org.kalibro.Range;
import org.kalibro.desktop.ComponentWrapperDialog;
import org.kalibro.desktop.swingextension.list.TablePanelListener;

public final class MetricConfigurationPanelManualTest implements TablePanelListener<Range> {

	public static void main(String[] args) {
		new MetricConfigurationPanelManualTest(metricConfiguration("amloc"));
		new MetricConfigurationPanelManualTest(new MetricConfiguration(sc()));
	}

	private MetricConfigurationPanelManualTest(MetricConfiguration configuration) {
		MetricConfigurationPanel panel = new MetricConfigurationPanel();
		panel.addRangesListener(this);
		panel.set(configuration);
		new ComponentWrapperDialog("MetricConfigurationPanel", panel).setVisible(true);
	}

	@Override
	public void add() {
		System.out.println("Add range");
	}

	@Override
	public void edit(Range range) {
		System.out.println("Edit " + range);
	}
}