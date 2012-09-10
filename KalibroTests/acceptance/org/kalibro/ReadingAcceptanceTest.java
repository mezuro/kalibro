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
		reading = group.getReadings().get(0);
	}

	@After
	public void tearDown() {
		group.delete();
	}

	@Test(timeout = ACCEPTANCE_TIMEOUT)
	public void testCrud() {
		assertTrue(ReadingGroup.all().isEmpty());

		reading.save();
		assertSaved();

		reading.setLabel("ReadingAcceptanceTest label");
		assertDifferentFromSaved();

		reading.save();
		assertSaved();

		reading.delete();
		assertFalse(group.getReadings().contains(reading));
		assertFalse(ReadingGroup.all().get(0).getReadings().contains(reading));
	}

	private void assertSaved() {
		assertDeepEquals(reading, ReadingGroup.all().get(0).getReadings().get(0));
	}

	private void assertDifferentFromSaved() {
		Reading saved = ReadingGroup.all().get(0).getReadings().get(0);
		assertFalse(reading.deepEquals(saved));
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