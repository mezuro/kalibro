package org.kalibro;

import static org.junit.Assert.assertFalse;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kalibro.core.concurrent.VoidTask;

public class ReadingAcceptanceTest extends AcceptanceTest {

	private Reading reading;
	private ReadingGroup group;

	@Before
	public void setUp() {
		group = loadFixture("scholar", ReadingGroup.class);
		group.save();
		reading = group.getReadings().get(0);
	}

	@After
	public void tearDown() {
		group.delete();
	}

	@Test
	public void testCrud() {
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

	@Test
	public void readingIsRequiredToBeInGroup() {
		assertThat(save()).throwsException().withMessage("Reading is not in any group.");
	}

	private VoidTask save() {
		return new VoidTask() {

			@Override
			public void perform() {
				new Reading().save();
			}
		};
	}
}