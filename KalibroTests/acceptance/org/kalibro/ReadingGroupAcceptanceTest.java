package org.kalibro;

import static org.junit.Assert.*;

import java.io.File;

import javax.persistence.RollbackException;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.Theory;
import org.kalibro.core.Environment;
import org.kalibro.core.concurrent.VoidTask;
import org.kalibro.tests.AcceptanceTest;

public class ReadingGroupAcceptanceTest extends AcceptanceTest {

	private File file;
	private ReadingGroup group;

	@Before
	public void setUp() {
		group = loadFixture("scholar", ReadingGroup.class);
		file = new File(Environment.dotKalibro(), "ReadingGroup-exported.yml");
		file.deleteOnExit();
	}

	@Theory
	public void testCrud(SupportedDatabase databaseType) {
		changeDatabase(databaseType);
		assertNotSaved();

		group.save();
		assertSaved();

		group.setDescription("Another description");
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
	public void nameShouldBeRequiredAndUnique(SupportedDatabase databaseType) {
		changeDatabase(databaseType);
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
	public void shouldImportAndExportAsYaml() throws Exception {
		group.exportTo(file);
		String expectedYaml = loadResource("ReadingGroup-scholar.yml");
		assertEquals(expectedYaml, FileUtils.readFileToString(file));
		assertDeepEquals(group, ReadingGroup.importFrom(file));
	}
}