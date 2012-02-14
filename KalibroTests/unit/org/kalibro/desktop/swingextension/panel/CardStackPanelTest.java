package org.kalibro.desktop.swingextension.panel;

import java.awt.CardLayout;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;
import org.kalibro.desktop.configuration.ConfigurationPanel;
import org.kalibro.desktop.configuration.MetricPanel;
import org.kalibro.desktop.configuration.RangePanel;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

public class CardStackPanelTest extends KalibroTestCase {

	private RangePanel rangePanel;
	private MetricPanel metricPanel;
	private ConfigurationPanel configurationPanel;

	private CardStackPanel cardStack;
	private CardLayout layout;

	@Before
	public void setUp() {
		createSamplePanels();
		cardStack = PowerMockito.spy(new CardStackPanel());
		spyLayout();
	}

	private void createSamplePanels() {
		rangePanel = new RangePanel();
		metricPanel = new MetricPanel();
		configurationPanel = new ConfigurationPanel();
	}

	private void spyLayout() {
		layout = (CardLayout) cardStack.getLayout();
		layout = PowerMockito.spy(layout);
		cardStack.setLayout(layout);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldAddOnPush() {
		cardStack.push(configurationPanel);
		Mockito.verify(cardStack).add(configurationPanel, configurationPanel.getName());

		cardStack.push(metricPanel);
		Mockito.verify(cardStack).add(metricPanel, metricPanel.getName());

		cardStack.push(rangePanel);
		Mockito.verify(cardStack).add(rangePanel, rangePanel.getName());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldShowTopOnPush() {
		cardStack.push(configurationPanel);
		Mockito.verify(layout).show(cardStack, configurationPanel.getName());

		cardStack.push(metricPanel);
		Mockito.verify(layout).show(cardStack, metricPanel.getName());

		cardStack.push(rangePanel);
		Mockito.verify(layout).show(cardStack, rangePanel.getName());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRemoveOnPop() {
		cardStack.push(configurationPanel);
		cardStack.push(metricPanel);
		cardStack.push(rangePanel);

		cardStack.pop();
		Mockito.verify(cardStack).remove(rangePanel);

		cardStack.pop();
		Mockito.verify(cardStack).remove(metricPanel);

		cardStack.pop();
		Mockito.verify(cardStack).remove(configurationPanel);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldShowPreviousTopOnPop() {
		cardStack.push(configurationPanel);
		cardStack.push(metricPanel);
		cardStack.push(rangePanel);
		Mockito.reset(layout);

		cardStack.pop();
		Mockito.verify(layout).show(cardStack, metricPanel.getName());

		cardStack.pop();
		Mockito.verify(layout).show(cardStack, configurationPanel.getName());
	}
}