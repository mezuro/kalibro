package org.kalibro.desktop.swingextension.panel;

import static org.junit.Assert.*;

import java.awt.Component;

import javax.swing.JPanel;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.tests.UnitTest;

public class TabbedPanelTest extends UnitTest implements TabbedPanelListener {

	private JPanel firstPanel, secondPanel, thirdPanel;

	private Component lastTab, newTab;

	private TabbedPanel panel;

	@Before
	public void setUp() {
		firstPanel = new JPanel();
		secondPanel = new JPanel();
		thirdPanel = new JPanel();
		panel = new TabbedPanel();
		panel.addTab("First", firstPanel);
		panel.addTab("Second", secondPanel);
		panel.addTab("Third", thirdPanel);
		panel.addPanelListener(this);
	}

	@Test
	public void checkOrder() {
		assertSame(firstPanel, panel.getComponentAt(0));
		assertSame(secondPanel, panel.getComponentAt(1));
		assertSame(thirdPanel, panel.getComponentAt(2));
	}

	@Test
	public void checkTitles() {
		assertSame("First", panel.getTitleAt(0));
		assertSame("Second", panel.getTitleAt(1));
		assertSame("Third", panel.getTitleAt(2));
	}

	@Test
	public void shouldSetTitle() {
		panel.setTitle("The first panel", firstPanel);
		assertEquals("The first panel", panel.getTitleAt(0));

		panel.setTitle("The third panel", thirdPanel);
		assertEquals("The third panel", panel.getTitleAt(2));
	}

	@Test
	public void firstTabShouldBeSelectedByDefault() {
		assertSame(firstPanel, panel.getSelectedComponent());
	}

	@Test
	public void testChangeTab() {
		panel.setSelectedComponent(secondPanel);
		assertChange(firstPanel, secondPanel);

		panel.setSelectedComponent(thirdPanel);
		assertChange(secondPanel, thirdPanel);

		panel.setSelectedComponent(firstPanel);
		assertChange(thirdPanel, firstPanel);

		panel.setSelectedComponent(thirdPanel);
		assertChange(firstPanel, thirdPanel);

		panel.setSelectedComponent(secondPanel);
		assertChange(thirdPanel, secondPanel);

		panel.setSelectedComponent(firstPanel);
		assertChange(secondPanel, firstPanel);
	}

	private void assertChange(Component expectedLast, Component expectedNew) {
		assertSame(expectedLast, lastTab);
		assertSame(expectedNew, newTab);
	}

	@Override
	public void tabChanged(Component lastComponent, Component newComponent) {
		lastTab = lastComponent;
		newTab = newComponent;
	}
}