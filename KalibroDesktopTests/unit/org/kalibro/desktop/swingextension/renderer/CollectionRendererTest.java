package org.kalibro.desktop.swingextension.renderer;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.tests.UnitTest;

public class CollectionRendererTest extends UnitTest {

	private CollectionRenderer renderer;

	@Before
	public void setUp() {
		renderer = new CollectionRenderer();
	}

	@Test
	public void shouldRenderCollection() {
		assertTrue(renderer.canRender(Arrays.asList("42")));
	}

	@Test
	public void shouldNotRenderAnythingButCollection() {
		assertFalse(renderer.canRender(true));
		assertFalse(renderer.canRender(Color.MAGENTA));
		assertFalse(renderer.canRender(42.0));
		assertFalse(renderer.canRender(null));
	}

	@Test
	public void shouldRenderListString() {
		assertEquals("My, list", render(Arrays.asList("My", "list")));
		assertEquals("", render(new ArrayList<String>()));
	}

	private String render(Collection<?> value) {
		return renderer.render(value).getText();
	}
}