package org.kalibro.desktop.configuration;

import static org.kalibro.tests.UnitTest.loadFixture;

import org.kalibro.CompoundMetric;
import org.kalibro.MetricConfiguration;
import org.kalibro.Range;
import org.kalibro.desktop.swingextension.table.TablePanelListener;
import org.kalibro.desktop.tests.ComponentWrapperDialog;

public final class MetricConfigurationPanelManualTest implements TablePanelListener<Range> {

	public static void main(String[] args) {
		new MetricConfigurationPanelManualTest(loadFixture("lcom4", MetricConfiguration.class));
		new MetricConfigurationPanelManualTest(new MetricConfiguration(loadFixture("sc", CompoundMetric.class)));
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