package org.kalibro;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kalibro.core.concurrent.Task;

public class ReadingAcceptanceTest extends AcceptanceTest {

	private Reading reading;
	private ReadingGroup readingGroup;

	@Before
	public void setUp() {
		readingGroup = loadFixture("readingGroup-scholar", ReadingGroup.class);
		reading = readingGroup.getReadings().get(0);
		readingGroup.save();
	}

	@After
	public void tearDown() {
		readingGroup.delete();
	}

	@Test(timeout = ACCEPTANCE_TIMEOUT)
	public void shouldSaveAndDeleteIndividually() {
		reading.setLabel("new label");
		reading.save();
		assertDeepList(ReadingGroup.all(), readingGroup);

		reading.delete();
		assertFalse(readingGroup.getReadings().contains(reading));

		reading.save();
		assertTrue(readingGroup.getReadings().contains(reading));
	}

	@Test(timeout = ACCEPTANCE_TIMEOUT)
	public void shouldRequireToBeInGroup() {
		checkKalibroException(new Task() {

			@Override
			protected void perform() throws Throwable {
				new Reading().save();
			}
		}, "Reading is not in any group.");
	}
}