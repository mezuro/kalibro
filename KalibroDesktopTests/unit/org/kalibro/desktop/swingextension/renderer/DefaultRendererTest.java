package org.kalibro.desktop.swingextension.renderer;

import static org.junit.Assert.*;

import java.awt.Component;
import java.util.Arrays;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.model.Range;
import org.kalibro.desktop.swingextension.Label;
import org.kalibro.desktop.swingextension.field.BooleanField;

public class DefaultRendererTest extends KalibroTestCase {

	private DefaultRenderer renderer;

	@Before
	public void setUp() {
		renderer = new DefaultRenderer();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRenderStringByDefault() {
		JLabel component = (JLabel) render(new Range(1.0, 2.0));
		assertEquals("[1.0, 2.0[", component.getText());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldHandleAnything() {
		assertClassEquals(BooleanField.class, render(true));
		assertClassEquals(Label.class, render(42.0));
		assertClassEquals(JPanel.class, render(null));
		assertClassEquals(JLabel.class, render("My string"));
		assertClassEquals(JLabel.class, render(this));
		assertClassEquals(JLabel.class, render(Arrays.asList("My", "list")));
	}

	private Component render(Object value) {
		assertTrue(renderer.canRender(value));
		return renderer.render(value);
	}
}