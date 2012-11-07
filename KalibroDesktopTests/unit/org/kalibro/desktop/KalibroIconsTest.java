package org.kalibro.desktop;

import static org.junit.Assert.assertEquals;
import static org.kalibro.desktop.KalibroIcons.*;

import org.junit.Test;
import org.kalibro.tests.UtilityClassTest;

public class KalibroIconsTest extends UtilityClassTest {

	@Test
	public void shouldGetIconAndImages() {
		verifyIcon(KALIBRO);
	}

	private void verifyIcon(String name) {
		assertEquals(icon(name).getImage(), image(name));
	}
}