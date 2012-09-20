package org.kalibro;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ConfigurationAcceptanceTest extends AcceptanceTest {

	private Configuration configuration;

	@Before
	public void setUp() {
		configuration = loadFixture("default", Configuration.class);
	}

	@Test
	public void testCrud() {
		assertNotSaved();

		configuration.save();
		assertSaved();

		configuration.setDescription("Another test description");
		assertFalse(Configuration.all().get(0).deepEquals(configuration));

		configuration.save();
		assertSaved();

		configuration.delete();
		assertNotSaved();
	}

	private void assertSaved() {
		assertDeepList(Configuration.all(), configuration);
	}

	private void assertNotSaved() {
		assertTrue(Configuration.all().isEmpty());
	}

}
