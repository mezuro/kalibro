package org.kalibro.core.persistence.record;

import static org.junit.Assert.*;

import org.junit.Test;
import org.kalibro.Granularity;
import org.kalibro.Module;
import org.kalibro.ModuleResult;

public class ModuleResultRecordTest extends RecordTest {

	@Override
	protected void verifyColumns() {
		shouldHaveId();
		assertColumn("processing", Long.class).isRequired();
		assertColumn("moduleName", String.class).isRequired();
		assertColumn("moduleGranularity", String.class).isRequired();
		assertColumn("grade", Long.class).isNullable();
		assertColumn("height", Integer.class).isRequired();
		assertColumn("parent", Long.class).isNullable();
	}

	@Test
	public void shouldTurnNullGradeIntoNan() {
		assertDoubleEquals(Double.NaN, new ModuleResultRecord().grade());
	}

	@Test
	public void shouldRetrieveParentId() {
		ModuleResult moduleResult = (ModuleResult) entity;
		ModuleResultRecord record = (ModuleResultRecord) dto;
		assertEquals(moduleResult.getParent().getId(), record.parentId());
	}

	@Test
	public void checkNullParent() {
		assertNull(new ModuleResultRecord(new ModuleResult(null, new Module(Granularity.SOFTWARE))).parentId());
	}
}