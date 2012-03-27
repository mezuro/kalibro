package org.kalibro.core.persistence.database.entities;

import static org.kalibro.core.model.ProjectFixtures.*;

import java.util.Arrays;
import java.util.Collection;

import org.kalibro.DtoTestCase;
import org.kalibro.KalibroException;
import org.kalibro.core.model.Project;

public class ProjectRecordTest extends DtoTestCase<Project, ProjectRecord> {

	@Override
	protected ProjectRecord newDtoUsingDefaultConstructor() {
		return new ProjectRecord();
	}

	@Override
	protected Collection<Project> entitiesForTestingConversion() {
		Project normal = helloWorld();
		Project withError = helloWorld();
		withError.setError(new KalibroException("ProjectRecordTest", new Exception()));
		return Arrays.asList(normal, withError);
	}

	@Override
	protected ProjectRecord createDto(Project project) {
		return new ProjectRecord(project);
	}
}