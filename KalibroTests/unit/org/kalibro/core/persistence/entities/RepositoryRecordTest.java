package org.kalibro.core.persistence.entities;

import static org.kalibro.core.model.RepositoryFixtures.helloWorldRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.kalibro.DtoTestCase;
import org.kalibro.core.model.Repository;
import org.kalibro.core.model.enums.RepositoryType;

public class RepositoryRecordTest extends DtoTestCase<Repository, RepositoryRecord> {

	@Override
	protected RepositoryRecord newDtoUsingDefaultConstructor() {
		return new RepositoryRecord();
	}

	@Override
	protected Collection<Repository> entitiesForTestingConversion() {
		List<Repository> helloWorldRepositories = new ArrayList<Repository>();
		for (RepositoryType repositoryType : RepositoryType.values())
			helloWorldRepositories.add(helloWorldRepository(repositoryType));
		return helloWorldRepositories;
	}

	@Override
	protected RepositoryRecord createDto(Repository repository) {
		return new RepositoryRecord(repository, null);
	}
}