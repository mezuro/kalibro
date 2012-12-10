package org.kalibro.dto;

import static org.junit.Assert.assertEquals;

import java.util.Random;

import org.junit.Test;
import org.kalibro.Project;
import org.kalibro.dao.RepositoryDao;
import org.powermock.reflect.Whitebox;

public class ProjectDtoTest extends AbstractDtoTest<Project> {

	@Override
	protected Project loadFixture() {
		Project project = new Project("ProjectDtoTest name");
		Whitebox.setInternalState(project, "id", new Random().nextLong());
		return project;
	}

	@Override
	protected void registerLazyLoadExpectations() {
		whenLazy(RepositoryDao.class, "repositoriesOf", entity.getId()).thenReturn(entity.getRepositories());
	}

	@Test
	public void shouldConvertNullDescriptionIntoEmptyString() throws Exception {
		when(dto, "description").thenReturn(null);
		assertEquals("", dto.convert().getDescription());
	}
}