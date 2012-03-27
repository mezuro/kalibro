package org.kalibro.core.persistence.database.entities;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.kalibro.DtoTestCase;
import org.kalibro.core.model.BaseTool;
import org.kalibro.core.model.BaseToolFixtures;

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

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRetrieveName() {
		assertEquals("BaseToolRecordTest", new BaseToolRecord("BaseToolRecordTest").getName());
	}
}