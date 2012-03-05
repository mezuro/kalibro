package org.kalibro.desktop.configuration;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;

import javax.swing.WindowConstants;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.model.Configuration;
import org.kalibro.core.model.ConfigurationFixtures;
import org.kalibro.core.model.MetricConfiguration;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.*")
@PrepareOnlyThisForTest(ConfigurationFrame.class)
public class ConfigurationFrameTest extends KalibroTestCase {

	private MetricConfigurationController metricConfigurationController;
	private Configuration configuration;

	private ConfigurationFrame frame;
	private ConfigurationPanel panel;

	@Before
	public void setUp() throws Exception {
		configuration = ConfigurationFixtures.simpleConfiguration();
		mockPanel();
		mockMetricConfigurationController();
		frame = new ConfigurationFrame(configuration);
	}

	private void mockPanel() throws Exception {
		panel = PowerMockito.spy(new ConfigurationPanel());
		PowerMockito.when(panel.get()).thenReturn(configuration);
		PowerMockito.whenNew(ConfigurationPanel.class).withNoArguments().thenReturn(panel);
	}

	private void mockMetricConfigurationController() throws Exception {
		metricConfigurationController = PowerMockito.mock(MetricConfigurationController.class);
		PowerMockito.whenNew(MetricConfigurationController.class).withArguments(same(configuration), any())
			.thenReturn(metricConfigurationController);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void titleShouldBeConfigurationName() {
		assertEquals(configuration.getName(), frame.getTitle());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldDoNothingOnCloseByDefault() {
		assertEquals(WindowConstants.DO_NOTHING_ON_CLOSE, frame.getDefaultCloseOperation());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldBeResizable() {
		assertTrue(frame.isResizable());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldBeClosable() {
		assertTrue(frame.isClosable());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldBeMaximizable() {
		assertTrue(frame.isMaximizable());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldBeIconifiable() {
		assertTrue(frame.isIconifiable());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldShowConfigurationOnPanel() {
		Mockito.verify(panel).set(configuration);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldListenToMetricConfigurationsPanel() {
		Mockito.verify(panel).addMetricConfigurationsPanelListener(frame);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldAddMetricConfiguration() {
		frame.add();
		InOrder order = Mockito.inOrder(panel, metricConfigurationController, panel);
		order.verify(panel).get();
		order.verify(metricConfigurationController).add();
		order.verify(panel).set(configuration);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldEditMetricConfiguration() {
		MetricConfiguration metricConfiguration = configuration.getMetricConfigurations().iterator().next();
		frame.edit(metricConfiguration);
		InOrder order = Mockito.inOrder(panel, metricConfigurationController, panel);
		order.verify(panel).get();
		order.verify(metricConfigurationController).edit(metricConfiguration);
		order.verify(panel).set(configuration);
	}
}