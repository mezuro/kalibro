package org.kalibro.dto;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.kalibro.Configuration;
import org.kalibro.Repository;
import org.kalibro.RepositoryType;
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
		whenLazy(ProjectDao.class, "projectOf", entity.getId()).thenReturn(null);
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
}