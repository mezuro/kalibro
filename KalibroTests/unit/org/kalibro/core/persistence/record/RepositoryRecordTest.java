package org.kalibro.core.persistence.record;

import static org.kalibro.core.loaders.RepositoryFixtures.helloWorldRepository;

import org.kalibro.Repository;
import org.kalibro.RepositoryType;

public class RepositoryRecordTest extends RecordTest<Repository> {

	@Override
	protected Repository loadFixture() {
		return helloWorldRepository(RepositoryType.GIT);
	}
}