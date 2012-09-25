package org.kalibro.service.xml;

import static org.kalibro.ProjectFixtures.helloWorld;

import org.kalibro.Project;

public class ProjectXmlRequestTest extends XmlTest<Project> {

	@Override
	protected Project loadFixture() {
		return helloWorld();
	}
}