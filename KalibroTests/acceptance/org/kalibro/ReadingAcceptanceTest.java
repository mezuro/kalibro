package org.kalibro;

import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.Theory;
import org.kalibro.core.concurrent.VoidTask;
import org.kalibro.tests.AcceptanceTest;

public class ReadingAcceptanceTest extends AcceptanceTest {

	private Reading reading;
	private ReadingGroup group;

	@Before
	public void setUp() {
		group = loadFixture("scholar", ReadingGroup.class);
		reading = group.getReadings().first();
	}

	@Theory
	public void testCrud(SupportedDatabase databaseType) {
		resetDatabase(databaseType);
		group.save();
		assertDeepEquals(reading, saved());

		reading.setLabel("ReadingAcceptanceTest label");
		assertFalse(reading.deepEquals(saved()));

		reading.save();
		assertDeepEquals(reading, saved());

		reading.delete();
		assertFalse(group.getReadings().contains(reading));
		assertFalse(ReadingGroup.all().first().getReadings().contains(reading));
	}

	private Reading saved() {
		return ReadingGroup.all().first().getReadings().first();
	}

	@Test
	public void readingIsRequiredToBeInGroup() {
		assertThat(saveNew()).throwsException().withMessage("Reading is not in any group.");
	}

	private VoidTask saveNew() {
		return new VoidTask() {

			@Override
			protected void perform() {
				new Reading().save();
			}
		};
	}
}