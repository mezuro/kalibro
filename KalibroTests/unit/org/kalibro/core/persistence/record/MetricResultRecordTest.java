package org.kalibro.core.persistence.record;

import static org.junit.Assert.*;

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
		shouldHaveId();
		assertManyToOne("moduleResult", ModuleResultRecord.class).isLazy().isRequired();
		assertManyToOne("configuration", MetricConfigurationSnapshotRecord.class).isEager().isRequired();
		assertColumn("value", Long.class).isRequired();
		assertOneToOne("error", ThrowableRecord.class).isEager().isOptional();
		assertOneToMany("descendantResults").isLazy().isMappedBy("metricResult");
	}

	@Test
	public void shouldConvertNullErrorForNormalResult() {
		MetricResult normalResult = new MetricResult(new MetricConfiguration(), 42.0);
		normalResult.addDescendantResult(28.0);
		assertNull(new MetricResultRecord(normalResult).error());
	}
}