package org.kalibro.service.xml;

import static org.kalibro.ProjectFixtures.newHelloWorld;

import org.kalibro.KalibroException;
import org.kalibro.Project;

public class ProjectXmlTest extends XmlTest<Project> {

	@Override
	protected Project loadFixture() {
		Project fixture = newHelloWorld();
		fixture.setError(new KalibroException("ProjectXmlTest", new Exception()));
		return fixture;
	}
}