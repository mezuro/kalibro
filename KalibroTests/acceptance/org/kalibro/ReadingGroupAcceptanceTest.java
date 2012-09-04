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
	public void shouldSaveRetrieveAndDelete() {
		assertTrue(ReadingGroup.all().isEmpty());

		group.save();
		assertDeepList(ReadingGroup.all(), group);

		group.delete();
		assertTrue(ReadingGroup.all().isEmpty());
	}

	@Test(timeout = ACCEPTANCE_TIMEOUT)
	public void nameShouldBeRequired() {
		group.setName("");
		checkKalibroException(new Task() {

			@Override
			protected void perform() throws Throwable {
				group.save();
			}
		}, "Reading group requires name.", RollbackException.class);
	}

	@Test(timeout = ACCEPTANCE_TIMEOUT)
	public void descriptionShouldNotBeRequired() {
		group.setDescription("");
		group.save();
		assertDeepList(ReadingGroup.all(), group);
	}

	@Test(timeout = ACCEPTANCE_TIMEOUT)
	public void nameShouldBeUnique() {
		group.save();
		group = loadFixture("readingGroup-scholar", ReadingGroup.class);
		checkKalibroException(new Task() {

			@Override
			protected void perform() throws Throwable {
				group.save();
			}
		}, "Reading group named \"Scholar\" already exists.", RollbackException.class);
	}

	@Test(timeout = ACCEPTANCE_TIMEOUT)
	public void shouldAddReadings() {
		group.save();

		group.add(new Reading("new label", 42.0, Color.MAGENTA));
		assertFalse(ReadingGroup.all().get(0).deepEquals(group));

		group.save();
		assertDeepList(ReadingGroup.all(), group);
	}

	@Test(timeout = ACCEPTANCE_TIMEOUT)
	public void shouldNotHaveDuplicateLabelsInGroup() {
		checkKalibroException(new Task() {

			@Override
			protected void perform() throws Throwable {
				group.add(new Reading("Good", 42.0, Color.WHITE));
			}
		}, "Reading with label 'Good' already exists in the group.");
	}

	@Test(timeout = ACCEPTANCE_TIMEOUT)
	public void shouldNotHaveDuplicateGradesInGroup() {
		checkKalibroException(new Task() {

			@Override
			protected void perform() throws Throwable {
				group.add(new Reading("new label", 0.0, Color.WHITE));
			}
		}, "Reading with grade 0.0 already exists in the group.");
	}

	@Test(timeout = ACCEPTANCE_TIMEOUT)
	public void shouldImportAndExportAsYaml() throws Exception {
		group.exportTo(file);
		String expectedYaml = loadResource("readingGroup-scholar.yml");
		assertEquals(expectedYaml, FileUtils.readFileToString(file));
		assertDeepEquals(group, ReadingGroup.importFrom(file));
	}
}