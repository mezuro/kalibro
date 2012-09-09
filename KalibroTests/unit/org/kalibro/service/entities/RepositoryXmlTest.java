package org.kalibro.service.entities;

import static org.kalibro.core.model.RepositoryFixtures.helloWorldRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.kalibro.DtoTestCase;
import org.kalibro.core.model.Repository;
import org.kalibro.core.model.enums.RepositoryType;

public class RepositoryXmlTest extends DtoTestCase<Repository, RepositoryXml> {

	@Override
	protected RepositoryXml newDtoUsingDefaultConstructor() {
		return new RepositoryXml();
	}

	@Override
	protected Collection<Repository> entitiesForTestingConversion() {
		List<Repository> helloWorldRepositories = new ArrayList<Repository>();
		for (RepositoryType repositoryType : RepositoryType.values())
			helloWorldRepositories.add(helloWorldRepository(repositoryType));
		return helloWorldRepositories;
	}

	@Override
	protected RepositoryXml createDto(Repository repository) {
		return new RepositoryXml(repository);
	}
}