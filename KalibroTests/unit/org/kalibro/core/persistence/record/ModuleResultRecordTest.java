package org.kalibro.core.persistence.record;

import static org.junit.Assert.*;

import org.junit.Test;
import org.kalibro.Granularity;
import org.kalibro.Module;
import org.kalibro.ModuleResult;

public class ModuleResultRecordTest extends RecordTest {

	@Override
	protected void verifyColumns() {
		assertManyToOne("processing", ProcessingRecord.class).isRequired();
		shouldHaveId();
		assertColumn("moduleName", String.class).isRequired();
		assertColumn("moduleGranularity", String.class).isRequired();
		assertColumn("grade", Long.class).isNullable();
		assertManyToOne("parent", ModuleResultRecord.class).isOptional();
		assertOneToMany("children").doesNotCascade().isMappedBy("parent");
		assertOneToMany("metricResults").cascades().isMappedBy("moduleResult");
	}

	@Test
	public void shouldConstructWithId() {
		Long id = mock(Long.class);
		assertSame(id, new ModuleResultRecord(id).id());
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