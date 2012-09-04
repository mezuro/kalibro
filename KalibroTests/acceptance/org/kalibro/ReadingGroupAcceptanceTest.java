package org.kalibro;

import static org.junit.Assert.*;

import java.awt.Color;
import java.io.File;

import javax.persistence.RollbackException;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kalibro.core.Environment;
import org.kalibro.core.concurrent.Task;

public class ReadingGroupAcceptanceTest extends AcceptanceTest {

	private File file;
	private ReadingGroup group;

	@Before
	public void setUp() {
		group = loadFixture("readingGroup-scholar", ReadingGroup.class);
		file = new File(Environment.dotKalibro(), "scholar.yml");
		file.deleteOnExit();
	}

	@After
	public void tearDown() {
		for (ReadingGroup each : ReadingGroup.all())
			each.delete();
	}

	@Test(timeout = ACCEPTANCE_TIMEOUT)
	public void testCrud() {
		assertNotSaved();

		group.save();
		assertSaved();

		group.addReading(newReading());
		assertDifferentFromSaved();

		group.save();
		assertSaved();

		group.delete();
		assertNotSaved();
	}

	private void assertNotSaved() {
		assertNull(group.getId());
		assertTrue(ReadingGroup.all().isEmpty());
	}

	private void assertSaved() {
		assertNotNull(group.getId());
		assertDeepList(ReadingGroup.all(), group);
	}

	private void assertDifferentFromSaved() {
		ReadingGroup saved = ReadingGroup.all().get(0);
		assertFalse(saved.deepEquals(group));
	}

	@Test(timeout = ACCEPTANCE_TIMEOUT)
	public void nameShouldBeRequiredAndUnique() {
		group.setName("");
		checkExceptionOnSave("Reading group requires name.", RollbackException.class);

		group.setName("Scholar");
		group.save();

		group = new ReadingGroup("Scholar");
		checkExceptionOnSave("Reading group named \"Scholar\" already exists.", RollbackException.class);
	}

	private void checkExceptionOnSave(String exceptionMessage, Class<? extends Throwable> expectedCause) {
		checkKalibroException(new Task() {

			@Override
			protected void perform() throws Throwable {
				group.save();
			}
		}, exceptionMessage, expectedCause);
	}

	@Test(timeout = ACCEPTANCE_TIMEOUT)
	public void readingsInSameGroupShouldNotHaveDuplicateLabelsOrGrade() {
		Reading reading, existent = group.getReadings().get(0);
		String label = existent.getLabel();
		Double grade = existent.getGrade();

		reading = newReading();
		reading.setLabel(label);
		checkExceptionOnAddReading(reading, "Reading with label \"" + label + "\" already exists in the group.");

		reading = newReading();
		reading.setGrade(grade);
		checkExceptionOnAddReading(reading, "Reading with grade " + grade + " already exists in the group.");
	}

	private Reading newReading() {
		return new Reading("ReadingGroupAcceptanceTest label", 42.0, Color.MAGENTA);
	}

	private void checkExceptionOnAddReading(final Reading reading, String message) {
		checkKalibroException(new Task() {

			@Override
			protected void perform() throws Throwable {
				group.addReading(reading);
			}
		}, message);
	}

	@Test(timeout = ACCEPTANCE_TIMEOUT)
	public void shouldImportAndExportAsYaml() throws Exception {
		group.exportTo(file);
		String expectedYaml = loadResource("readingGroup-scholar.yml");
		assertEquals(expectedYaml, FileUtils.readFileToString(file));
		assertDeepEquals(group, ReadingGroup.importFrom(file));
	}
}