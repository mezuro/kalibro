package org.kalibro;

import static org.junit.Assert.*;
import static org.powermock.api.mockito.PowerMockito.*;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.core.abstractentity.AbstractEntity;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(AbstractEntity.class)
public class ReadingGroupTest extends TestCase {

	private File file;
	private ReadingGroup group;

	@Before
	public void setUp() {
		file = mock(File.class);
		group = spy(loadFixture("readingGroup-scholar", ReadingGroup.class));
		mockStatic(AbstractEntity.class);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldGetAllReadings() {
		ReadingGroup.all();
		fail("mock database access for testing");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldImportFromFile() throws Exception {
		when(AbstractEntity.class, "importFrom", file, ReadingGroup.class).thenReturn(group);
		assertSame(group, ReadingGroup.importFrom(file));
		verifyPrivate(AbstractEntity.class).invoke("importFrom", file, ReadingGroup.class);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkDefaultGroup() {
		group = new ReadingGroup();
		assertEquals("", group.getName());
		assertEquals("", group.getDescription());
		assertTrue(group.getReadings().isEmpty());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldCheckIfIsSaved() {
		group.save();
		fail("mock database access for testing");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSave() {
		group.isSaved();
		fail("mock database access for testing");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldDelete() {
		group.delete();
		fail("mock database access for testing");
	}
}