package org.kalibro.desktop.swingextension.renderer;

import static org.junit.Assert.*;

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
	public void shouldRenderCollections() {
		assertTrue(renderer.canRender(set(true, false)));
		assertTrue(renderer.canRender(list(42.0)));
		assertTrue(renderer.canRender(list((Object) null)));
		assertTrue(renderer.canRender(sortedSet("String")));
	}

	@Test
	public void shouldRenderOnlyCollections() {
		assertFalse(renderer.canRender(true));
		assertFalse(renderer.canRender(42.0));
		assertFalse(renderer.canRender(null));
		assertFalse(renderer.canRender("String"));

		assertFalse(renderer.canRender(new Object()));
	}

	@Test
	public void shouldRenderStringList() {
		assertEquals("", render(list()));
		assertEquals("first, second, third", render(list("first", "second", "third")));
	}

	private String render(Collection<?> value) {
		return renderer.render(value).getText();
	}
}