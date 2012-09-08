package org.kalibro;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kalibro.core.concurrent.Task;

public class ReadingAcceptanceTest extends AcceptanceTest {

	private Reading reading;
	private ReadingGroup group;

	@Before
	public void setUp() {
		group = loadFixture("readingGroup-scholar", ReadingGroup.class);
		group.save();
		reading = group.getReadings().get(0);
	}

	@After
	public void tearDown() {
		group.delete();
	}

	@Test(timeout = ACCEPTANCE_TIMEOUT)
	public void testCrud() {
		assertSaved();

		reading.setLabel("ReadingAcceptanceTest label");
		assertDifferentFromSaved();

		reading.delete();
		assertNotSaved();
	}

	private void assertSaved() {
		assertNotNull(reading.getId());
		group = ReadingGroup.all().get(0);
		assertDeepEquals(reading, group.getReadings().get(0));
	}

	private void assertDifferentFromSaved() {
		Reading saved = ReadingGroup.all().get(0).getReadings().get(0);
		assertFalse(reading.deepEquals(saved));
	}

	private void assertNotSaved() {
		assertNull(reading.getId());
		group = ReadingGroup.all().get(0);
		assertFalse(group.getReadings().contains(reading));
	}

	@Test(timeout = ACCEPTANCE_TIMEOUT)
	public void readingIsRequiredToBeInGroup() {
		checkKalibroException(new Task() {

			@Override
			protected void perform() throws Throwable {
				new Reading().save();
			}
		}, "Reading is not in any group.");
	}
}