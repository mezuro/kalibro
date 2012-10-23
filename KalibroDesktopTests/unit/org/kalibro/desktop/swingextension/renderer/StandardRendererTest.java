package org.kalibro.desktop.swingextension.renderer;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.tests.UnitTest;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

public class StandardRendererTest extends UnitTest {

	private StandardRenderer renderer;

	@Before
	public void setUp() {
		renderer = PowerMockito.spy(new NullRenderer());
	}

	@Test
	public void shouldRenderIgnoringContext() {
		renderer.render("42", "24");
		Mockito.verify(renderer).render("42");
	}
}