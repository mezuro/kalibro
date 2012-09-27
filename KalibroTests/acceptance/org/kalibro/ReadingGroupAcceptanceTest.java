package org.kalibro;

import static org.junit.Assert.*;

import java.awt.Color;
import java.io.File;

import javax.persistence.RollbackException;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.kalibro.core.Environment;
import org.kalibro.core.concurrent.VoidTask;
import org.kalibro.tests.AcceptanceTest;

@RunWith(Parameterized.class)
public class ReadingGroupAcceptanceTest extends AcceptanceTest {

	private File file;
	private ReadingGroup group;

	public ReadingGroupAcceptanceTest(SupportedDatabase databaseType) {
		super(databaseType);
	}

	@Before
	public void setUp() {
		group = loadFixture("scholar", ReadingGroup.class);
		file = new File(Environment.dotKalibro(), "ReadingGroup-exported.yml");
		file.deleteOnExit();
	}

	@After
	public void tearDown() {
		for (ReadingGroup each : ReadingGroup.all())
			each.delete();
	}

	@Test
	public void testCrud() {
		assertNotSaved();

		group.save();
		assertSaved();

		group.addReading(newReading());
		assertFalse(ReadingGroup.all().first().deepEquals(group));

		group.save();
		assertSaved();

		group.delete();
		assertNotSaved();
	}

	private void assertNotSaved() {
		assertTrue(ReadingGroup.all().isEmpty());
	}

	private void assertSaved() {
		assertDeepEquals(asSet(group), ReadingGroup.all());
	}

	@Test
	public void nameShouldBeRequiredAndUnique() {
		group.setName(" ");
		assertThat(save()).throwsException().withMessage("Reading group requires name.");

		group.setName("Unique");
		group.save();

		group = new ReadingGroup("Unique");
		assertThat(save()).doThrow(RollbackException.class);
	}

	private VoidTask save() {
		return new VoidTask() {

			@Override
			protected void perform() {
				group.save();
			}
		};
	}

	@Test
	public void readingsInSameGroupShouldNotHaveDuplicateLabelsOrGrade() {
		Reading reading, existent = group.getReadings().first();
		String label = existent.getLabel();
		Double grade = existent.getGrade();

		reading = newReading();
		reading.setLabel(label);
		assertThat(addReading(reading)).throwsException()
			.withMessage("Reading with label \"" + label + "\" already exists in the group.");

		reading = newReading();
		reading.setGrade(grade);
		assertThat(addReading(reading)).throwsException()
			.withMessage("Reading with grade " + grade + " already exists in the group.");
	}

	private Reading newReading() {
		return new Reading("ReadingGroupAcceptanceTest label", 42.0, Color.MAGENTA);
	}

	private VoidTask addReading(final Reading reading) {
		return new VoidTask() {

			@Override
			protected void perform() {
				group.addReading(reading);
			}
		};
	}

	@Test
	public void shouldImportAndExportAsYaml() throws Exception {
		group.exportTo(file);
		String expectedYaml = loadResource("ReadingGroup-scholar.yml");
		assertEquals(expectedYaml, FileUtils.readFileToString(file));
		assertDeepEquals(group, ReadingGroup.importFrom(file));
	}
}