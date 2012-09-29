package org.kalibro.service.xml;

import static org.kalibro.ProjectResultFixtures.helloWorldResult;

import org.kalibro.ProjectResult;

public class ProjectResultXmlTest extends XmlTest<ProjectResult> {

	@Override
	protected ProjectResult loadFixture() {
		return helloWorldResult();
	}
}