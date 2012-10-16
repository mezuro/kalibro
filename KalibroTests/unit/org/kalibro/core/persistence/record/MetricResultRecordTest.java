package org.kalibro.core.persistence.record;

public class MetricResultRecordTest extends RecordTest {

	@Override
	protected void verifyColumns() {
		assertManyToOne("moduleResult", ModuleResultRecord.class);
		shouldHaveId();
		assertManyToOne("configuration", MetricConfigurationSnapshotRecord.class).isRequired();
		assertColumn("value", Long.class).isRequired();
		assertOneToMany("descendantResults").mappedBy("metricResult");
//		assertOneToOne();
	}
}