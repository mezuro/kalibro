package org.kalibro.service.xml;

import static org.kalibro.core.model.RepositoryFixtures.helloWorldRepository;

import org.kalibro.Repository;
import org.kalibro.RepositoryType;

public class RepositoryXmlTest extends XmlTest<Repository> {

	@Override
	protected Repository loadFixture() {
		return helloWorldRepository(RepositoryType.SUBVERSION);
	}
}