package org.kalibro;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.*;

import java.awt.Color;
import java.io.File;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.core.abstractentity.AbstractEntity;
import org.kalibro.core.persistence.dao.DaoFactory;
import org.kalibro.core.persistence.dao.ReadingGroupDao;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({AbstractEntity.class, DaoFactory.class})
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
		ReadingGroupDao dao = mockReadingGroupDao();
		List<ReadingGroup> list = mock(List.class);
		when(dao.all()).thenReturn(list);
		assertSame(list, ReadingGroup.all());
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
	public void shouldAddReading() {
		Reading reading = new Reading("label", 42.0, Color.magenta);
		group.add(reading);
		assertTrue(group.getReadings().contains(reading));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSave() {
		ReadingGroupDao dao = mockReadingGroupDao();
		group.save();
		verify(dao).save(group);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldDelete() {
		ReadingGroupDao dao = mockReadingGroupDao();
		group.delete();
		verify(dao).delete(group);
	}

	private ReadingGroupDao mockReadingGroupDao() {
		ReadingGroupDao readingGroupDao = mock(ReadingGroupDao.class);
		mockStatic(DaoFactory.class);
		when(DaoFactory.getReadingGroupDao()).thenReturn(readingGroupDao);
		return readingGroupDao;
	}
}