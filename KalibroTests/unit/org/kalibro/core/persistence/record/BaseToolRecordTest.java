package org.kalibro.core.persistence.record;

import static org.junit.Assert.assertEquals;
import static org.kalibro.core.model.BaseToolFixtures.*;

import java.util.Arrays;
import java.util.Collection;

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
		return Arrays.asList(analizo(), analizoStub());
	}

	@Override
	protected BaseToolRecord createDto(BaseTool baseTool) {
		return new BaseToolRecord(baseTool);
	}

	@Test
	public void shouldRetrieveName() {
		assertEquals("BaseToolRecordTest", new BaseToolRecord("BaseToolRecordTest").getName());
	}

	@Test
	public void shouldThrowErrorForCollectorClassNotFound() {
		final BaseToolRecord dto = createDto(analizoStub());
		Whitebox.setInternalState(dto, "collectorClass", "inexistent");
		assertThat(new Task() {

			@Override
			public void perform() throws Throwable {
				dto.convert();
			}
		}).throwsError().withMessage("Could not find collector class").withCause(ClassNotFoundException.class);
	}
}