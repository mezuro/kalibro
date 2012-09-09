package org.kalibro.desktop.configuration;

import static org.junit.Assert.assertEquals;
import static org.kalibro.core.model.MetricConfigurationFixtures.metricConfiguration;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.TestCase;
import org.kalibro.core.model.MetricConfiguration;
import org.kalibro.core.model.Range;
import org.kalibro.core.model.enums.Statistic;
import org.kalibro.desktop.ComponentFinder;
import org.kalibro.desktop.swingextension.Button;
import org.kalibro.desktop.swingextension.field.ChoiceField;
import org.kalibro.desktop.swingextension.field.DoubleField;
import org.kalibro.desktop.swingextension.field.StringField;
import org.kalibro.desktop.swingextension.list.Table;
import org.kalibro.desktop.swingextension.list.TablePanelListener;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

public class MetricConfigurationPanelTest extends TestCase {

	private MetricConfiguration configuration;

	private TablePanelListener<Range> rangesListener;
	private MetricConfigurationPanel panel;
	private ComponentFinder finder;

	@Before
	public void setUp() {
		configuration = metricConfiguration("amloc");
		rangesListener = PowerMockito.mock(TablePanelListener.class);
		panel = new MetricConfigurationPanel();
		panel.addRangesListener(rangesListener);
		finder = new ComponentFinder(panel);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldGet() {
		codeField().set(configuration.getCode());
		metricPanel().set(configuration.getMetric());
		weightField().set(configuration.getWeight());
		aggregationFormField().set(configuration.getAggregationForm());
		rangesTable().setData(configuration.getRanges());
		assertDeepEquals(configuration, panel.get());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSet() {
		panel.set(configuration);
		assertEquals(configuration.getCode(), codeField().get());
		assertDeepEquals(configuration.getMetric(), metricPanel().get());
		assertDoubleEquals(configuration.getWeight(), weightField().get());
		assertEquals(configuration.getAggregationForm(), aggregationFormField().get());
		assertDeepEquals(configuration.getRanges(), rangesTable().getData());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotifyRangesPanelListener() {
		buttonAdd().doClick();
		Mockito.verify(rangesListener).add();
	}

	private StringField codeField() {
		return finder.find("code", StringField.class);
	}

	private MetricPanel metricPanel() {
		return finder.find("metric", MetricPanel.class);
	}

	private DoubleField weightField() {
		return finder.find("weight", DoubleField.class);
	}

	private ChoiceField<Statistic> aggregationFormField() {
		return finder.find("aggregationForm", ChoiceField.class);
	}

	private Table<Range> rangesTable() {
		return finder.find("ranges", Table.class);
	}

	private Button buttonAdd() {
		return finder.find("add", Button.class);
	}
}