package org.kalibro.core.persistence.record;

import org.kalibro.Range;
import org.kalibro.core.model.RangeFixtures;
import org.kalibro.core.model.RangeLabel;

public class RangeRecordTest extends RecordTest<Range> {

	@Override
	protected Range loadFixture() {
		return RangeFixtures.newRange("amloc", RangeLabel.REGULAR);
	}
}