package org.kalibro.desktop.swingextension;

import static org.junit.Assert.assertEquals;

import java.net.URL;
import java.util.Random;

import org.junit.Test;
import org.kalibro.core.concurrent.VoidTask;
import org.kalibro.tests.UnitTest;

public class IconTest extends UnitTest {

	private static final int WIDTH = new Random().nextInt(100);
	private static final int HEIGHT = new Random().nextInt(100);

	@Test
	public void shouldNotAcceptInvalidResource() {
		assertThat(new VoidTask() {

			@Override
			protected void perform() {
				new Icon(url("inexistent.gif"));
			}
		}).doThrow(NullPointerException.class);
	}

	@Test
	public void shouldScaleForSize() {
		Icon icon = new Icon(url("waiting.gif"));
		icon = icon.scaleForSize(WIDTH, HEIGHT);
		assertEquals(WIDTH, icon.getIconWidth());
		assertEquals(HEIGHT, icon.getIconHeight());
	}

	private URL url(String resourceName) {
		return getClass().getResource(resourceName);
	}
}