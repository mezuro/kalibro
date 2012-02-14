package org.kalibro.desktop.swingextension.panel;

import static org.junit.Assert.*;

import java.awt.Component;

import javax.swing.JPanel;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;

public class TabbedPanelTest extends KalibroTestCase {

	private JPanel configurationPanel, metricPanel, rangePanel;

	private Component lastTab, newTab;

	private TabbedPanel panel;

	@Before
	public void setUp() {
		configurationPanel = new JPanel();
		metricPanel = new JPanel();
		rangePanel = new JPanel();
		panel = new TabbedPanel();
		panel.addTab("Configuration", configurationPanel);
		panel.addTab("Metric", metricPanel);
		panel.addTab("Range", rangePanel);
		panel.addPanelListener(new TabbedPanelListener() {

			@Override
			public void tabChanged(Component lastComponent, Component newComponent) {
				lastTab = lastComponent;
				newTab = newComponent;
			}
		});
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkOrder() {
		assertSame(configurationPanel, panel.getComponentAt(0));
		assertSame(metricPanel, panel.getComponentAt(1));
		assertSame(rangePanel, panel.getComponentAt(2));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkTitles() {
		assertSame("Configuration", panel.getTitleAt(0));
		assertSame("Metric", panel.getTitleAt(1));
		assertSame("Range", panel.getTitleAt(2));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testSetTitle() {
		panel.setTitle("The configuration", configurationPanel);
		assertEquals("The configuration", panel.getTitleAt(0));

		panel.setTitle("The range", rangePanel);
		assertEquals("The range", panel.getTitleAt(2));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void firstTabShouldBeSelectedByDefault() {
		assertSame(configurationPanel, panel.getSelectedComponent());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testChangeTab() {
		panel.setSelectedComponent(metricPanel);
		assertChange(configurationPanel, metricPanel);

		panel.setSelectedComponent(rangePanel);
		assertChange(metricPanel, rangePanel);

		panel.setSelectedComponent(configurationPanel);
		assertChange(rangePanel, configurationPanel);

		panel.setSelectedComponent(rangePanel);
		assertChange(configurationPanel, rangePanel);

		panel.setSelectedComponent(metricPanel);
		assertChange(rangePanel, metricPanel);

		panel.setSelectedComponent(configurationPanel);
		assertChange(metricPanel, configurationPanel);
	}

	private void assertChange(Component expectedLast, Component expectedNew) {
		assertSame(expectedLast, lastTab);
		assertSame(expectedNew, newTab);
	}
}