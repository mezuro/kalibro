package org.kalibro.core.persistence.record;

import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.kalibro.MetricConfiguration;
import org.kalibro.MetricResult;
import org.powermock.reflect.Whitebox;

public class MetricResultRecordTest extends RecordTest {

	@Override
	public void shouldRetrieveEntityFieldsAsTheyWere() throws Exception {
		MetricConfiguration configuration = ((MetricResult) entity).getConfiguration();
		MetricConfigurationSnapshotRecord record = mock(MetricConfigurationSnapshotRecord.class);
		when(record.convert()).thenReturn(configuration);
		Whitebox.setInternalState(dto, "configuration", record);
		super.shouldRetrieveEntityFieldsAsTheyWere();
	}

	@Override
	protected void verifyColumns() {
		assertManyToOne("moduleResult", ModuleResultRecord.class).isRequired();
		assertManyToOne("configuration", MetricConfigurationSnapshotRecord.class).isRequired();
		shouldHaveId();
		assertColumn("value", Long.class).isRequired();
		shouldHaveError("error");
		assertOneToMany("descendantResults").cascades().isMappedBy("metricResult");
	}

	@Test
	public void shouldConvertNullErrorForNormalResult() {
		MetricResult normalResult = new MetricResult(new MetricConfiguration(), 42.0);
		normalResult.addDescendantResult(28.0);
		assertNull(new MetricResultRecord(normalResult).error());
	}
}