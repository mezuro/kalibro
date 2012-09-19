package org.kalibro.service.entities;

import static org.kalibro.core.model.ProjectFixtures.helloWorld;

import java.util.Arrays;
import java.util.Collection;

import org.kalibro.DtoTestCase;
import org.kalibro.core.model.Project;

public class RawProjectXmlTest extends DtoTestCase<Project, RawProjectXml> {

	@Override
	protected RawProjectXml newDtoUsingDefaultConstructor() {
		return new RawProjectXml();
	}

	@Override
	protected Collection<Project> entitiesForTestingConversion() {
		return Arrays.asList(helloWorld());
	}

	@Override
	protected RawProjectXml createDto(Project project) {
		return new RawProjectXml(project);
	}

	@Override
	protected void assertCorrectConversion(Project original, Project converted) {
		converted.setState(original.getState());
		assertDeepEquals(original, converted);
	}
}