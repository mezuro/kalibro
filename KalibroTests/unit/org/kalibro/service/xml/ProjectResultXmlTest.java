package org.kalibro.service.xml;

import static org.kalibro.core.model.ProjectResultFixtures.helloWorldResult;

import java.util.Arrays;
import java.util.Collection;

import org.kalibro.DtoTestCase;
import org.kalibro.core.model.ProjectResult;

public class ProjectResultXmlTest extends DtoTestCase<ProjectResult, ProjectResultXml> {

	@Override
	protected ProjectResultXml newDtoUsingDefaultConstructor() {
		return new ProjectResultXml();
	}

	@Override
	protected Collection<ProjectResult> entitiesForTestingConversion() {
		return Arrays.asList(helloWorldResult());
	}

	@Override
	protected ProjectResultXml createDto(ProjectResult projectResult) {
		return new ProjectResultXml(projectResult);
	}
}