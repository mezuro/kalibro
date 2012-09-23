package org.kalibro.core.persistence.record;

import static org.kalibro.core.model.RepositoryFixtures.helloWorldRepository;

import org.kalibro.core.model.Repository;
import org.kalibro.core.model.enums.RepositoryType;

public class RepositoryRecordTest extends RecordTest<Repository> {

	@Override
	protected Repository loadFixture() {
		return helloWorldRepository(RepositoryType.GIT);
	}
}