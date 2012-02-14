package org.kalibro.service.entities;

import static org.junit.Assert.*;
import static org.kalibro.core.model.ProjectFixtures.*;

import java.util.Arrays;
import java.util.Collection;

import org.kalibro.DtoTestCase;
import org.kalibro.core.model.Project;
import org.kalibro.core.model.enums.ProjectState;

public class ProjectXmlTest extends DtoTestCase<Project, ProjectXml> {

	@Override
	protected ProjectXml newDtoUsingDefaultConstructor() {
		return new ProjectXml();
	}

	@Override
	protected Collection<Project> entitiesForTestingConversion() {
		Project normal = helloWorld();
		Project withError = helloWorld();
		withError.setError(new Exception());
		return Arrays.asList(normal, withError);
	}

	@Override
	protected ProjectXml createDto(Project project) {
		return new ProjectXml(project);
	}

	@Override
	protected void assertCorrectConversion(Project original, Project converted) {
		if (original.getState() == ProjectState.ERROR) {
			assertEquals(ProjectState.ERROR, converted.getState());
			new ErrorXmlTest().assertCorrectConversion(original.getError(), converted.getError());
			original.setState(ProjectState.LOADING);
			converted.setState(ProjectState.LOADING);
		}
		assertDeepEquals(original, converted);
	}
}