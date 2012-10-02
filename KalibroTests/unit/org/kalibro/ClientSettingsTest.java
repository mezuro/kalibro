package org.kalibro;

import static org.junit.Assert.*;

import org.junit.Test;
import org.kalibro.tests.UnitTest;

public class ClientSettingsTest extends UnitTest {

	@Test
	public void checkConstruction() {
		assertEquals("http://localhost:8080/KalibroService/", new ClientSettings().getServiceAddress());
	}

	@Test
	public void shouldCommentServiceAddress() {
		assertTrue(new ClientSettings().toString().contains(" # Address of the remote Kalibro Service\n"));
	}
}