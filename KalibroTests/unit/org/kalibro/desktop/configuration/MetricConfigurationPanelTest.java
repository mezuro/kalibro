package org.kalibro.desktop.configuration;

import static org.junit.Assert.*;
import static org.kalibro.core.model.MetricConfigurationFixtures.*;
import static org.mockito.Matchers.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;
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

public class MetricConfigurationPanelTest extends KalibroTestCase {

	private MetricConfiguration configuration;

	private MetricConfigurationPanel panel;
	private ComponentFinder finder;

	private ActionListener buttonListener;
	private TablePanelListener<Range> tableListener;

	@Before
	public void setUp() {
		configuration = configuration("amloc");
		panel = new MetricConfigurationPanel();
		finder = new ComponentFinder(panel);
		createListeners();
	}

	private void createListeners() {
		buttonListener = PowerMockito.mock(ActionListener.class);
		tableListener = PowerMockito.mock(TablePanelListener.class);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldShow() {
		panel.set(configuration);
		assertEquals(configuration.getCode(), codeField().get());
		assertDeepEquals(configuration.getMetric(), metricPanel().get());
		assertDoubleEquals(configuration.getWeight(), weightField().get());
		assertEquals(configuration.getAggregationForm(), aggregationFormField().get());
		assertDeepEquals(configuration.getRanges(), rangesTable().getData());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRetrieve() {
		codeField().set(configuration.getCode());
		metricPanel().set(configuration.getMetric());
		weightField().set(configuration.getWeight());
		aggregationFormField().set(configuration.getAggregationForm());
		rangesTable().setData(configuration.getRanges());
		assertDeepEquals(configuration, panel.get());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotifyRangesPanelListener() {
		panel.addRangesPanelListener(tableListener);
		button("add").doClick();
		Mockito.verify(tableListener).add();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotifyButtonListenerOnCancel() {
		panel.addButtonListener(buttonListener);
		button("cancel").doClick();
		Mockito.verify(buttonListener).actionPerformed(any(ActionEvent.class));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotifyButtonListener() {
		panel.addButtonListener(buttonListener);
		button("ok").doClick();
		Mockito.verify(buttonListener).actionPerformed(any(ActionEvent.class));
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

	private Button button(String name) {
		return finder.find(name, Button.class);
	}
}