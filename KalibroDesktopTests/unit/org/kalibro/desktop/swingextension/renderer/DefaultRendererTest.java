package org.kalibro.desktop.swingextension.renderer;

import static org.junit.Assert.*;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.Range;
import org.kalibro.desktop.swingextension.Label;
import org.kalibro.desktop.swingextension.field.BooleanField;
import org.kalibro.tests.UnitTest;

public class DefaultRendererTest extends UnitTest {

	private DefaultRenderer renderer;

	@Before
	public void setUp() {
		renderer = new DefaultRenderer();
	}

	@Test
	public void shouldRenderStringByDefault() {
		JLabel component = (JLabel) render(new Range(1.0, 2.0));
		assertEquals("[1.0, 2.0[", component.getText());
	}

	@Test
	public void shouldHandleAnything() {
		assertClassEquals(BooleanField.class, render(true));
		assertClassEquals(Label.class, render(42.0));
		assertClassEquals(JPanel.class, render(null));
		assertClassEquals(JLabel.class, render("My string"));
		assertClassEquals(JLabel.class, render(this));
		assertClassEquals(JLabel.class, render(list("My", "list")));
	}

	private Component render(Object value) {
		assertTrue(renderer.canRender(value));
		return renderer.render(value);
	}
}