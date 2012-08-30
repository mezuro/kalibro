package org.kalibro.desktop.swingextension.renderer;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.TestCase;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

public class StandardRendererTest extends TestCase {

	private StandardRenderer renderer;

	@Before
	public void setUp() {
		renderer = PowerMockito.spy(new NullRenderer());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRenderIgnoringContext() {
		renderer.render("42", "24");
		Mockito.verify(renderer).render("42");
	}
}