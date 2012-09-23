package org.kalibro.service.xml;

import static org.kalibro.core.model.ProjectFixtures.*;

import java.util.Arrays;
import java.util.Collection;

import org.kalibro.DtoTestCase;
import org.kalibro.KalibroException;
import org.kalibro.core.model.Project;

public class ProjectXmlTest extends DtoTestCase<Project, ProjectXml> {

	@Override
	protected ProjectXml newDtoUsingDefaultConstructor() {
		return new ProjectXml();
	}

	@Override
	protected Collection<Project> entitiesForTestingConversion() {
		Project normal = helloWorld();
		Project withError = newHelloWorld();
		withError.setError(new KalibroException("ProjectXmlTest", new Exception()));
		return Arrays.asList(normal, withError);
	}

	@Override
	protected ProjectXml createDto(Project project) {
		return new ProjectXml(project);
	}
}