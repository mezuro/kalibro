package org.kalibro.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.kalibro.TestCase;

public class KalibroImplTest extends TestCase {

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldGetVersion() {
		assertEquals("0.6", new KalibroImpl().version());
	}
}