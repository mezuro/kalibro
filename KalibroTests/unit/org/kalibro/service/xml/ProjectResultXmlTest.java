package org.kalibro.service.xml;

import static org.kalibro.core.model.ProjectResultFixtures.helloWorldResult;

import org.kalibro.core.model.ProjectResult;

public class ProjectResultXmlTest extends XmlTest<ProjectResult> {

	@Override
	protected ProjectResult loadFixture() {
		return helloWorldResult();
	}
}