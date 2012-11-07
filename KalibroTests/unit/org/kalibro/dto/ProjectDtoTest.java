package org.kalibro.dto;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.kalibro.Project;
import org.kalibro.dao.RepositoryDao;

public class ProjectDtoTest extends AbstractDtoTest<Project> {

	@Override
	protected Project loadFixture() {
		return new Project("ProjectDtoTest name");
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