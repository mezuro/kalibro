package org.kalibro.core.persistence.record;

public class ModuleResultRecordTest extends RecordTest {

	@Override
	protected void verifyColumns() {
		assertManyToOne("processing", ProcessingRecord.class).isRequired();
		shouldHaveId();
		assertOrderedElementCollection("moduleName");
		assertColumn("moduleGranularity", String.class).isRequired();
		assertColumn("grade", Long.class).isRequired();
		assertManyToOne("parent", ModuleResultRecord.class).isOptional();
		assertOneToMany("children").isMappedBy("parent");
	}
}