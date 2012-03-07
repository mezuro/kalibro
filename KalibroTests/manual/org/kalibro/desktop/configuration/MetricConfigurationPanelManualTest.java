package org.kalibro.desktop.configuration;

import static org.kalibro.core.model.CompoundMetricFixtures.*;
import static org.kalibro.core.model.MetricConfigurationFixtures.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.kalibro.core.model.MetricConfiguration;
import org.kalibro.core.model.Range;
import org.kalibro.desktop.ComponentWrapperDialog;
import org.kalibro.desktop.swingextension.Button;
import org.kalibro.desktop.swingextension.list.TablePanelListener;

public final class MetricConfigurationPanelManualTest implements ActionListener, TablePanelListener<Range> {

	public static void main(String[] args) {
		new MetricConfigurationPanelManualTest(configuration("amloc"));
		new MetricConfigurationPanelManualTest(new MetricConfiguration(sc()));
	}

	private MetricConfigurationPanelManualTest(MetricConfiguration configuration) {
		MetricConfigurationPanel panel = new MetricConfigurationPanel();
		panel.addRangesPanelListener(this);
		panel.addButtonListener(this);
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

	@Override
	public void actionPerformed(ActionEvent event) {
		Button source = (Button) event.getSource();
		System.out.println(source.getName());
	}
}