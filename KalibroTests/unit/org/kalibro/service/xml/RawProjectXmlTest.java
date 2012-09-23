package org.kalibro.service.xml;

import static org.kalibro.core.model.ProjectFixtures.helloWorld;

import org.kalibro.core.model.Project;

public class RawProjectXmlTest extends XmlTest<Project> {

	@Override
	protected Project loadFixture() {
		return helloWorld();
	}
}