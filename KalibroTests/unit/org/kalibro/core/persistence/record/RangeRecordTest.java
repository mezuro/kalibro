package org.kalibro.core.persistence.record;

import org.kalibro.core.model.Range;
import org.kalibro.core.model.RangeFixtures;
import org.kalibro.core.model.RangeLabel;

public class RangeRecordTest extends RecordTest<Range, RangeRecord> {

	@Override
	protected Range loadFixture() {
		return RangeFixtures.newRange("amloc", RangeLabel.REGULAR);
	}

	@Override
	protected Class<RangeRecord> dtoClass() {
		return RangeRecord.class;
	}
}