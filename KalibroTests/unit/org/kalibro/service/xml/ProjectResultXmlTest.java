package org.kalibro.service.xml;

import static org.kalibro.ProjectResultFixtures.helloWorldResult;

import org.kalibro.RepositoryResult;

public class ProjectResultXmlTest extends XmlTest<RepositoryResult> {

	@Override
	protected RepositoryResult loadFixture() {
		return helloWorldResult();
	}
}