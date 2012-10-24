package org.kalibro;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.tests.UnitTest;

public class ClientSettingsTest extends UnitTest {

	private ClientSettings settings;

	@Before
	public void setUp() {
		settings = new ClientSettings();
	}

	@Test
	public void checkConstruction() {
		assertEquals("http://localhost:8080/KalibroService/", settings.getServiceAddress());
	}

	@Test
	public void shouldCommentServiceAddress() {
		assertTrue(settings.toString().contains(" # Address of the remote Kalibro Service\n"));
	}
}