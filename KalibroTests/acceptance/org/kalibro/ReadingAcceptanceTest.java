package org.kalibro;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.kalibro.core.concurrent.VoidTask;
import org.kalibro.tests.AcceptanceTest;

@RunWith(Parameterized.class)
public class ReadingAcceptanceTest extends AcceptanceTest {

	private Reading reading;
	private ReadingGroup group;

	public ReadingAcceptanceTest(SupportedDatabase databaseType) {
		super(databaseType);
	}

	@Before
	public void setUp() {
		group = loadFixture("scholar", ReadingGroup.class);
		group.save();
		reading = group.getReadings().first();
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
		assertFalse(ReadingGroup.all().first().getReadings().contains(reading));
	}

	private void assertSaved() {
		assertDeepEquals(reading, ReadingGroup.all().first().getReadings().first());
	}

	private void assertDifferentFromSaved() {
		Reading saved = ReadingGroup.all().first().getReadings().first();
		assertFalse(reading.deepEquals(saved));
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

	@Test
	public void shouldNotSetConflictingLabelOrGrade() {
		assertThat(new VoidTask() {

			@Override
			protected void perform() throws Throwable {
				reading.setLabel("Excellent");
			}
		}).throwsException().withMessage("Reading with label \"Excellent\" already exists in the group.");
		assertThat(new VoidTask() {

			@Override
			protected void perform() throws Throwable {
				reading.setGrade(10.0);
			}
		}).throwsException().withMessage("Reading with grade 10.0 already exists in the group.");
		assertEquals("Terrible", reading.getLabel());
		assertDoubleEquals(0.0, reading.getGrade());
	}
}