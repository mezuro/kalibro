package org.kalibro.core.persistence;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;
import org.kalibro.Project;
import org.kalibro.core.persistence.record.ProjectRecord;
import org.powermock.core.classloader.annotations.PrepareForTest;

@PrepareForTest(ProjectDatabaseDao.class)
public class ProjectDatabaseDaoTest extends DatabaseDaoTestCase<Project, ProjectRecord, ProjectDatabaseDao> {

	private static final Long ID = new Random().nextLong();

	@Test
	public void shouldGetProjectOfRepository() {
		assertSame(entity, dao.projectOf(ID));

		String from = "Repository repository JOIN repository.project project";
		verify(dao).createRecordQuery(from, "repository.id = :repositoryId");
		verify(query).setParameter("repositoryId", ID);
	}

	@Test
	public void shouldSave() throws Exception {
		when(record.id()).thenReturn(ID);
		assertEquals(ID, dao.save(entity));

		verifyNew(ProjectRecord.class).withArguments(entity);
		verify(dao).save(record);
	}
}