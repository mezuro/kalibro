package org.kalibro.core.persistence.record;

import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.Range;
import org.kalibro.Reading;
import org.kalibro.dao.ReadingDao;
import org.kalibro.dto.DaoLazyLoader;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DaoLazyLoader.class)
public class RangeRecordTest extends RecordTest {

	@Override
	public void setUp() throws Exception {
		super.setUp();
		Long id = Whitebox.getInternalState(entity, "id");
		Reading reading = Whitebox.getInternalState(entity, "reading");
		mockStatic(DaoLazyLoader.class);
		when(DaoLazyLoader.createProxy(ReadingDao.class, "readingOf", id)).thenReturn(reading);
	}

	@Override
	protected void verifyColumns() {
		assertManyToOne("configuration", MetricConfigurationRecord.class).isRequired();
		shouldHaveId();
		assertColumn("beginning", Long.class).isRequired();
		assertColumn("end", Long.class).isRequired();
		assertColumn("comments", String.class).isNullable();
		assertManyToOne("reading", ReadingRecord.class).isOptional();
	}

	@Test
	public void checkNullReading() {
		assertNull(new RangeRecord(new Range()).reading());
	}
}