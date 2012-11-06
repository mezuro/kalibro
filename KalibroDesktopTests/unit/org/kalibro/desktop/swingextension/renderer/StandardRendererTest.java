package org.kalibro.desktop.swingextension.renderer;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.tests.UnitTest;

public class StandardRendererTest extends UnitTest {

	private Object value, context;

	private StandardRenderer renderer;

	@Before
	public void setUp() throws Exception {
		value = mock(Object.class);
		context = mock(Object.class);
		renderer = mockAbstract(StandardRenderer.class);
		doReturn(null).when(renderer).render(value);

		renderer.render(value, context);
	}

	@Test
	public void shouldRenderValue() {
		verify(renderer).render(value);
	}

	@Test
	public void shouldIgnoreContext() {
		verifyZeroInteractions(context);
	}
}