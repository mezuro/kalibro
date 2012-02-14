package org.kalibro.core.persistence.database.entities;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.kalibro.DtoTestCase;
import org.kalibro.core.model.BaseTool;
import org.kalibro.core.model.BaseToolFixtures;
import org.kalibro.core.persistence.database.entities.BaseToolRecord;

public class BaseToolRecordTest extends DtoTestCase<BaseTool, BaseToolRecord> {

	@Override
	protected BaseToolRecord newDtoUsingDefaultConstructor() {
		return new BaseToolRecord();
	}

	@Override
	protected Collection<BaseTool> entitiesForTestingConversion() {
		return Arrays.asList(BaseToolFixtures.analizoStub());
	}

	@Override
	protected BaseToolRecord createDto(BaseTool baseTool) {
		return new BaseToolRecord(baseTool);
	}

	@Override
	protected void assertCorrectConversion(BaseTool original, BaseTool converted) {
		assertEquals(original.getName(), converted.getName());
		assertEquals(original.getDescription(), converted.getDescription());
		assertEquals(original.getCollectorClass(), converted.getCollectorClass());
		assertEquals(original.getSupportedMetrics(), converted.getSupportedMetrics());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRetrieveName() {
		assertEquals("My base tool", new BaseToolRecord("My base tool").getName());
	}
}