package org.kalibro;

import static org.junit.Assert.assertFalse;

import java.awt.Color;

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
		reading.setLabel("label");
		reading.save();
		assertDeepList(ReadingGroup.all(), readingGroup);

		reading.delete();
		assertFalse(readingGroup.getReadings().contains(reading));
	}

	@Test(timeout = ACCEPTANCE_TIMEOUT)
	public void shouldRequireToBeInGroup() {
		reading.delete();
		checkKalibroException(new Task() {

			@Override
			protected void perform() throws Throwable {
				reading.save();
			}
		}, "Reading is not in any group.");
	}

	@Test(timeout = ACCEPTANCE_TIMEOUT)
	public void shouldNotHaveDuplicateLabelsInGroup() {
		checkKalibroException(new Task() {

			@Override
			protected void perform() throws Throwable {
				readingGroup.add(new Reading(reading.getLabel(), 42.0, Color.WHITE));
			}
		}, "Reading with label '" + reading.getLabel() + "' already exists in the group.");
	}

	@Test(timeout = ACCEPTANCE_TIMEOUT)
	public void shouldNotHaveDuplicateGradesInGroup() {
		checkKalibroException(new Task() {

			@Override
			protected void perform() throws Throwable {
				readingGroup.add(new Reading("label", reading.getGrade(), Color.WHITE));
			}
		}, "Reading with grade " + reading.getGrade() + " already exists in the group.");
	}
}