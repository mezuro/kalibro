package org.kalibro.desktop.configuration;

import static org.junit.Assert.*;
import static org.kalibro.core.model.ConfigurationFixtures.*;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.model.Configuration;
import org.kalibro.core.model.MetricConfiguration;
import org.kalibro.desktop.ComponentFinder;
import org.kalibro.desktop.swingextension.Button;
import org.kalibro.desktop.swingextension.field.TextField;
import org.kalibro.desktop.swingextension.field.UneditableField;
import org.kalibro.desktop.swingextension.list.Table;
import org.kalibro.desktop.swingextension.list.TablePanelListener;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

public class ConfigurationPanelTest extends KalibroTestCase {

	private Configuration configuration;

	private ConfigurationPanel panel;
	private ComponentFinder finder;

	@Before
	public void setUp() {
		configuration = simpleConfiguration();
		panel = new ConfigurationPanel();
		finder = new ComponentFinder(panel);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldGet() {
		nameField().set(configuration.getName());
		descriptionField().set(configuration.getDescription());
		metricConfigurationsTable().setData(configuration.getMetricConfigurations());
		assertDeepEquals(configuration, panel.get());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSet() {
		panel.set(configuration);
		assertEquals(configuration.getName(), nameField().get());
		assertEquals(configuration.getDescription(), descriptionField().get());
		assertDeepEquals(configuration.getMetricConfigurations(), metricConfigurationsTable().getData());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotifyRangesPanelListener() {
		TablePanelListener<MetricConfiguration> listener = PowerMockito.mock(TablePanelListener.class);
		panel.addMetricConfigurationsListener(listener);
		finder.find("add", Button.class).doClick();
		Mockito.verify(listener).add();
	}

	private UneditableField<String> nameField() {
		return finder.find("name", UneditableField.class);
	}

	private TextField descriptionField() {
		return finder.find("description", TextField.class);
	}

	private Table<MetricConfiguration> metricConfigurationsTable() {
		return finder.find("metricConfigurations", Table.class);
	}
}