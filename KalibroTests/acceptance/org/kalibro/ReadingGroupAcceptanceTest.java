package org.kalibro;

import static org.junit.Assert.*;

import java.io.File;
import java.util.TreeSet;

import javax.persistence.RollbackException;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.Theory;
import org.kalibro.core.Environment;
import org.kalibro.core.concurrent.TaskMatcher;
import org.kalibro.core.concurrent.VoidTask;
import org.kalibro.tests.AcceptanceTest;
import org.powermock.reflect.Whitebox;

public class ReadingGroupAcceptanceTest extends AcceptanceTest {

	private ReadingGroup group;

	@Before
	public void setUp() {
		group = loadFixture("scholar", ReadingGroup.class);
	}

	@Theory
	public void testCrud(SupportedDatabase databaseType) {
		resetDatabase(databaseType);
		assertNotSaved();

		group.save();
		assertSaved();

		group.setName("ReadingGroupAcceptanceTest name");
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
		assertDeepEquals(set(group), ReadingGroup.all());
	}

	@Theory
	public void shouldValidateOnSave(SupportedDatabase databaseType) {
		resetDatabase(databaseType);
		nameShouldBeUnique();
		nameShouldBeRequired();
	}

	private void nameShouldBeUnique() {
		new ReadingGroup(group.getName()).save();
		assertSave().doThrow(RollbackException.class);
	}

	private void nameShouldBeRequired() {
		group.setName(" ");
		assertSave().throwsException().withMessage("Reading group requires name.");
	}

	private TaskMatcher assertSave() {
		return assertThat(new VoidTask() {

			@Override
			protected void perform() {
				group.save();
			}
		});
	}

	@Theory
	public void deleteGroupShouldCascadeToReadings(SupportedDatabase databaseType) {
		resetDatabase(databaseType);
		group.save();
		Long id = group.getId();

		group.delete();
		group.setReadings(new TreeSet<Reading>());
		Whitebox.setInternalState(group, "id", id);
		group.save();

		ReadingGroup first = ReadingGroup.all().first();
		assertEquals(id, first.getId());
		assertTrue(first.getReadings().isEmpty());
	}

	@Test
	public void shouldImportAndExportAsYaml() throws Exception {
		File file = new File(Environment.dotKalibro(), "ReadingGroup-exported.yml");
		file.deleteOnExit();

		group.exportTo(file);
		assertEquals(loadResource("ReadingGroup-scholar.yml"), FileUtils.readFileToString(file));
		assertDeepEquals(group, ReadingGroup.importFrom(file));
	}
}