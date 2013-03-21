package org.kalibro.dto;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;
import org.kalibro.Configuration;
import org.kalibro.Project;
import org.kalibro.Repository;
import org.kalibro.RepositoryType;
import org.kalibro.dao.ConfigurationDao;
import org.kalibro.dao.ProjectDao;

public class RepositoryDtoTest extends AbstractDtoTest<Repository> {

	@Override
	protected Repository loadFixture() {
		Repository repository = new Repository("RepositoryDtoTest", RepositoryType.GIT, "RepositoryDtoTest address");
		repository.setConfiguration(loadFixture("sc", Configuration.class));
		return repository;
	}

	@Override
	protected void registerLazyLoadExpectations() throws Exception {
		Long configurationId = new Random().nextLong();
		doReturn(configurationId).when(dto, "configurationId");
		whenLazy(ConfigurationDao.class, "get", configurationId).thenReturn(entity.getConfiguration());

		Long projectId = new Random().nextLong();
		doReturn(projectId).when(dto, "projectId");
		whenLazy(ProjectDao.class, "get", projectId).thenReturn(new Project());
	}

	@Test
	public void shouldConvertNullDescriptionIntoEmptyString() throws Exception {
		when(dto, "description").thenReturn(null);
		assertEquals("", dto.convert().getDescription());
	}

	@Test
	public void shouldConvertNullLicenseIntoEmptyString() throws Exception {
		when(dto, "license").thenReturn(null);
		assertEquals("", dto.convert().getLicense());
	}

	@Test
	public void shouldRetrieveProjectWhenProjectIdIsNotNull() throws Exception {
		doCallRealMethod().when(dto, "projectId");
		assertNull(dto.convert().getProject());
	}
}