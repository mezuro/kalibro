package org.kalibro.core.persistence.record;

import static org.junit.Assert.*;

import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.OrderColumn;

import org.junit.Test;
import org.kalibro.Granularity;
import org.kalibro.Module;
import org.kalibro.ModuleResult;
import org.powermock.reflect.Whitebox;

public class ModuleResultRecordTest extends RecordTest {

	@Override
	protected void verifyColumns() {
		assertManyToOne("processing", ProcessingRecord.class).isRequired();
		shouldHaveId();
		assertOrderedElementCollection("moduleName");
		assertColumn("moduleGranularity", String.class).isRequired();
		assertColumn("grade", Long.class).isNullable();
		assertManyToOne("parent", ModuleResultRecord.class).isOptional();
		assertOneToMany("children").doesNotCascade().isMappedBy("parent");
		assertOneToMany("metricResults").cascades().isMappedBy("moduleResult");
	}

	private void assertOrderedElementCollection(String field) {
		assertOrdered(field);
		annotation(field, ElementCollection.class);
	}

	private void assertOrdered(String field) {
		assertFieldType(field, List.class);
		OrderColumn orderColumn = annotation(field, OrderColumn.class);
		assertEquals("Wrong @OrderColumn name.", "\"index\"", orderColumn.name());
		assertFalse("@OrderColumn should NOT be nullable.", orderColumn.nullable());
	}

	@Test
	public void shouldConstructWithId() {
		Long id = mock(Long.class);
		assertSame(id, new ModuleResultRecord(id).id());
	}

	@Test
	public void shouldConstructWithoutParent() {
		ModuleResult orphan = new ModuleResult(null, new Module(Granularity.SOFTWARE));
		assertNull(Whitebox.getInternalState(new ModuleResultRecord(orphan), "parent"));
	}

	@Test
	public void shouldTurnNullGradeIntoNan() {
		assertDoubleEquals(Double.NaN, new ModuleResultRecord().grade());
	}
}