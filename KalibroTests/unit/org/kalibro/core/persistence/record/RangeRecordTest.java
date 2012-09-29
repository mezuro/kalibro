package org.kalibro.core.persistence.record;

import org.kalibro.Range;
import org.kalibro.RangeFixtures;
import org.kalibro.RangeLabel;

public class RangeRecordTest extends RecordTest<Range> {

	@Override
	protected Range loadFixture() {
		return RangeFixtures.newRange("amloc", RangeLabel.REGULAR);
	}
}