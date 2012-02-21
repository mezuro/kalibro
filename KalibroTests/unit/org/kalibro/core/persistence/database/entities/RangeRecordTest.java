package org.kalibro.core.persistence.database.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.SortedSet;

import org.kalibro.DtoTestCase;
import org.kalibro.core.model.MetricConfigurationFixtures;
import org.kalibro.core.model.Range;

public class RangeRecordTest extends DtoTestCase<Range, RangeRecord> {

	@Override
	protected RangeRecord newDtoUsingDefaultConstructor() {
		return new RangeRecord();
	}

	@Override
	protected Collection<Range> entitiesForTestingConversion() {
		SortedSet<Range> ranges = MetricConfigurationFixtures.configuration("amloc").getRanges();
		return new ArrayList<Range>(ranges);
	}

	@Override
	protected RangeRecord createDto(Range range) {
		return new RangeRecord(range, null);
	}
}