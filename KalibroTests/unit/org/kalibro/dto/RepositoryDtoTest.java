package org.kalibro.dto;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;
import org.kalibro.Configuration;
import org.kalibro.Repository;
import org.kalibro.RepositoryType;
import org.kalibro.dao.ConfigurationDao;

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