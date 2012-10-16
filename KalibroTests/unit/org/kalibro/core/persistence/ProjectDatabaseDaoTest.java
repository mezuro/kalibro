package org.kalibro.core.persistence;

import static org.junit.Assert.assertEquals;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.Project;
import org.kalibro.core.persistence.record.ProjectRecord;
import org.kalibro.tests.UnitTest;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ProjectDatabaseDao.class)
public class ProjectDatabaseDaoTest extends UnitTest {

	private static final Long ID = new Random().nextLong();

	private Project project;
	private ProjectRecord record;

	private ProjectDatabaseDao dao;

	@Before
	public void setUp() throws Exception {
		project = mock(Project.class);
		record = mock(ProjectRecord.class);
		whenNew(ProjectRecord.class).withArguments(project).thenReturn(record);
		when(record.convert()).thenReturn(project);
		when(record.id()).thenReturn(ID);
		dao = spy(new ProjectDatabaseDao(null));
	}

	@Test
	public void shouldSave() {
		doReturn(record).when(dao).save(record);
		assertEquals(ID, dao.save(project));
		verify(dao).save(record);
	}
}