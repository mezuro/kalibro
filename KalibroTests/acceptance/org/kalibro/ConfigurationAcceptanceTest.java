package org.kalibro;

import static org.junit.Assert.*;

import javax.persistence.RollbackException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kalibro.core.concurrent.VoidTask;
import org.kalibro.tests.AcceptanceTest;

public class ConfigurationAcceptanceTest extends AcceptanceTest {

	private Configuration configuration;

	@Before
	public void setUp() {
		configuration = loadFixture("default", Configuration.class);
	}

	@After
	public void tearDown() {
		for (Configuration each : Configuration.all())
			each.delete();
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

	@Test
	public void nameShouldBeRequiredAndUnique() {
		configuration.setName(" ");
		assertThat(save()).throwsException().withMessage("Configuration requires name.");

		configuration.setName("Sample Configuration");
		configuration.save();

		configuration = new Configuration();
		configuration.setName("Sample Configuration");
		assertThat(save()).doThrow(RollbackException.class);
	}

	private VoidTask save() {
		return new VoidTask() {

			@Override
			protected void perform() {
				configuration.save();
			}
		};
	}
}