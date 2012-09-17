package org.kalibro.service.entities;

import static org.junit.Assert.assertEquals;
import static org.kalibro.core.model.BaseToolFixtures.*;

import java.util.Arrays;
import java.util.Collection;

import org.kalibro.DtoTestCase;
import org.kalibro.core.model.BaseTool;

public class BaseToolXmlTest extends DtoTestCase<BaseTool, BaseToolXml> {

	@Override
	protected BaseToolXml newDtoUsingDefaultConstructor() {
		return new BaseToolXml();
	}

	@Override
	protected Collection<BaseTool> entitiesForTestingConversion() {
		return Arrays.asList(analizo(), analizoStub());
	}

	@Override
	protected BaseToolXml createDto(BaseTool baseTool) {
		return new BaseToolXml(baseTool);
	}

	@Override
	public void assertCorrectConversion(BaseTool original, BaseTool converted) {
		assertEquals(original.getName(), converted.getName());
		assertEquals(original.getDescription(), converted.getDescription());
		assertDeepEquals(original.getSupportedMetrics(), converted.getSupportedMetrics());
	}
}