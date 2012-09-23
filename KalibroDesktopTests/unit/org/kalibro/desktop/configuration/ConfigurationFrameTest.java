package org.kalibro.desktop.configuration;

import static org.junit.Assert.*;
import static org.kalibro.core.model.ConfigurationFixtures.newConfiguration;

import javax.swing.JPanel;
import javax.swing.JTable;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.Configuration;
import org.kalibro.core.model.MetricConfiguration;
import org.kalibro.desktop.ComponentFinder;
import org.kalibro.desktop.swingextension.Button;
import org.kalibro.desktop.swingextension.panel.CardStackPanel;
import org.kalibro.tests.UnitTest;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.*")
@PrepareOnlyThisForTest(ConfigurationFrame.class)
public class ConfigurationFrameTest extends UnitTest {

	private Configuration configuration;
	private ConfigurationPanel panel;
	private MetricConfigurationController metricConfigurationController;

	private ConfigurationFrame frame;
	private ComponentFinder finder;

	@Before
	public void setUp() throws Exception {
		configuration = newConfiguration("loc");
		mockPanel();
		mockMetricConfigurationController();
		frame = new ConfigurationFrame(configuration);
		finder = new ComponentFinder(frame);
	}

	private void mockPanel() throws Exception {
		panel = PowerMockito.spy(new ConfigurationPanel());
		PowerMockito.whenNew(ConfigurationPanel.class).withNoArguments().thenReturn(panel);
	}

	private void mockMetricConfigurationController() throws Exception {
		metricConfigurationController = PowerMockito.mock(MetricConfigurationController.class);
		PowerMockito.whenNew(MetricConfigurationController.class).withArguments(eq(configuration), any())
			.thenReturn(metricConfigurationController);
	}

	@Test
	public void titleShouldHaveConfigurationName() {
		assertEquals(configuration.getName() + " - Configuration", frame.getTitle());
	}

	@Test
	public void shouldShowConfigurationOnPanel() {
		Mockito.verify(panel).set(configuration);
	}

	@Test
	public void shouldListenToMetricConfigurationsPanel() {
		Mockito.verify(panel).addMetricConfigurationsListener(frame);
	}

	@Test
	public void shouldAddMetricConfiguration() {
		finder.find("add", Button.class).doClick();

		Mockito.verify(panel).get();
		Mockito.verify(metricConfigurationController).addMetricConfiguration();
	}

	@Test
	public void shouldEditMetricConfiguration() {
		finder.find("metricConfigurations", JTable.class).getSelectionModel().setSelectionInterval(0, 0);
		finder.find("edit", Button.class).doClick();

		MetricConfiguration metricConfiguration = configuration.getMetricConfigurations().iterator().next();
		Mockito.verify(panel).get();
		Mockito.verify(metricConfigurationController).edit(metricConfiguration);
	}

	@Test
	public void shouldRetrieveConfiguration() {
		PowerMockito.when(panel.get()).thenReturn(configuration);
		assertSame(configuration, frame.get());
	}

	@Test
	public void shouldRefreshConfigurationWhenPanelIsRemovedFromCardStack() {
		CardStackPanel cardStack = (CardStackPanel) Whitebox.getInternalState(frame, "cardStack");
		cardStack.push(new JPanel());

		Mockito.reset(panel);
		cardStack.pop();
		Mockito.verify(panel).set(configuration);
	}
}