package org.kalibro.core.persistence.database.entities;

import static org.junit.Assert.*;
import static org.kalibro.core.model.ProjectFixtures.*;

import java.util.Arrays;
import java.util.Collection;

import org.kalibro.DtoTestCase;
import org.kalibro.core.model.Project;
import org.kalibro.core.model.enums.ProjectState;

public class ProjectRecordTest extends DtoTestCase<Project, ProjectRecord> {

	@Override
	protected ProjectRecord newDtoUsingDefaultConstructor() {
		return new ProjectRecord();
	}

	@Override
	protected Collection<Project> entitiesForTestingConversion() {
		Project normal = helloWorld();
		Project withError = helloWorld();
		withError.setError(new Exception());
		return Arrays.asList(normal, withError);
	}

	@Override
	protected ProjectRecord createDto(Project project) {
		return new ProjectRecord(project);
	}

	@Override
	protected void assertCorrectConversion(Project original, Project converted) {
		if (original.getState() == ProjectState.ERROR) {
			assertEquals(ProjectState.ERROR, converted.getState());
			new ErrorRecordTest().assertCorrectConversion(original.getError(), converted.getError());
			original.setState(ProjectState.LOADING);
			converted.setState(ProjectState.LOADING);
		}
		assertDeepEquals(original, converted);
	}
}