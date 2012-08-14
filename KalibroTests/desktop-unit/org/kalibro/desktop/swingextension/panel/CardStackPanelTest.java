package org.kalibro.desktop.swingextension.panel;

import static org.junit.Assert.*;

import java.awt.CardLayout;
import java.awt.Dimension;

import javax.swing.JPanel;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;
import org.kalibro.desktop.swingextension.field.StringField;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

public class CardStackPanelTest extends KalibroTestCase {

	private StringField base, middle, top;
	private CardLayout layout;

	private CardStackPanel cardStack;

	@Before
	public void setUp() {
		base = new StringField("base", 10);
		middle = new StringField("middle", 20);
		top = new StringField("top", 30);
		cardStack = PowerMockito.spy(new CardStackPanel());
		spyLayout();
	}

	private void spyLayout() {
		layout = (CardLayout) cardStack.getLayout();
		layout = PowerMockito.spy(layout);
		cardStack.setLayout(layout);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldAddOnPush() {
		cardStack.push(base);
		Mockito.verify(cardStack).add(base, base.getName());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldShowTopOnPush() {
		cardStack.push(base);
		Mockito.verify(layout).show(cardStack, base.getName());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRepaintOnPush() {
		cardStack.push(base);
		Mockito.verify(cardStack).repaint();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRemoveOnPop() {
		cardStack.push(base);
		cardStack.push(middle);
		cardStack.push(top);

		cardStack.pop();
		Mockito.verify(cardStack).remove(top);

		cardStack.pop();
		Mockito.verify(cardStack).remove(middle);

		cardStack.pop();
		Mockito.verify(cardStack).remove(base);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldShowPreviousTopOnPop() {
		cardStack.push(base);
		cardStack.push(middle);
		cardStack.push(top);
		Mockito.reset(layout);

		cardStack.pop();
		Mockito.verify(layout).show(cardStack, middle.getName());

		cardStack.pop();
		Mockito.verify(layout).show(cardStack, base.getName());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldAdjustSize() {
		Dimension preferredSize = new Dimension(1024, 768);
		Dimension minimumSize = new Dimension(640, 480);
		Dimension size = new Dimension(768, 640);
		JPanel panel = new JPanel();
		panel.setPreferredSize(preferredSize);
		panel.setMinimumSize(minimumSize);
		panel.setSize(size);
		cardStack.push(panel);
		assertEquals(preferredSize, cardStack.getPreferredSize());
		assertEquals(minimumSize, cardStack.getMinimumSize());
		assertEquals(size, cardStack.getSize());
	}
}