package org.kalibro.service.xml;

import static org.kalibro.ProjectFixtures.newHelloWorld;

import org.kalibro.KalibroException;
import org.kalibro.Project;

public class ProjectXmlResponseTest extends XmlTest<Project> {

	@Override
	protected Project loadFixture() {
		Project fixture = newHelloWorld();
		fixture.setError(new KalibroException("ProjectXmlResponseTest", new Exception()));
		return fixture;
	}
}