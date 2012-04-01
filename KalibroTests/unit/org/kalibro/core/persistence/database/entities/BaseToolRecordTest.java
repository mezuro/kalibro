package org.kalibro.core.persistence.database.entities;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;

import org.analizo.AnalizoStub;
import org.junit.Test;
import org.kalibro.DtoTestCase;
import org.kalibro.core.concurrent.Task;
import org.kalibro.core.model.BaseTool;
import org.powermock.reflect.Whitebox;

public class BaseToolRecordTest extends DtoTestCase<BaseTool, BaseToolRecord> {

	@Override
	protected BaseToolRecord newDtoUsingDefaultConstructor() {
		return new BaseToolRecord();
	}

	@Override
	protected Collection<BaseTool> entitiesForTestingConversion() {
		return Arrays.asList(new AnalizoStub().getBaseTool());
	}

	@Override
	protected BaseToolRecord createDto(BaseTool baseTool) {
		return new BaseToolRecord(baseTool);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRetrieveName() {
		assertEquals("BaseToolRecordTest", new BaseToolRecord("BaseToolRecordTest").getName());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldThrowErrorForCollectorClassNotFound() {
		final BaseToolRecord dto = createDto(new AnalizoStub().getBaseTool());
		Whitebox.setInternalState(dto, "collectorClass", "inexistent");
		checkKalibroError(new Task() {

			@Override
			protected void perform() throws Throwable {
				dto.convert();
			}
		}, "Could not find collector class", ClassNotFoundException.class);
	}
}