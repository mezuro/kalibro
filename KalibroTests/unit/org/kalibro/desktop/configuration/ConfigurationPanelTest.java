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
import org.kalibro.desktop.swingextension.field.StringField;
import org.kalibro.desktop.swingextension.field.TextField;
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
		configuration = kalibroConfiguration();
		panel = new ConfigurationPanel();
		finder = new ComponentFinder(panel);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldShow() {
		panel.show(configuration);
		assertEquals(configuration.getName(), nameField().getValue());
		assertEquals(configuration.getDescription(), descriptionField().getValue());
		assertDeepEquals(configuration.getMetricConfigurations(), metricConfigurationsTable().getData());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRetrieve() {
		nameField().setValue(configuration.getName());
		descriptionField().setValue(configuration.getDescription());
		metricConfigurationsTable().setData(configuration.getMetricConfigurations());
		assertDeepEquals(configuration, panel.retrieve());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotifyRangesPanelListener() {
		TablePanelListener<MetricConfiguration> listener = PowerMockito.mock(TablePanelListener.class);
		panel.addMetricConfigurationsPanelListener(listener);
		finder.find("add", Button.class).doClick();
		Mockito.verify(listener).add();
	}

	private StringField nameField() {
		return finder.find("name", StringField.class);
	}

	private TextField descriptionField() {
		return finder.find("description", TextField.class);
	}

	private Table<MetricConfiguration> metricConfigurationsTable() {
		return finder.find("metricConfigurations", Table.class);
	}
}