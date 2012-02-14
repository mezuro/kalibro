package org.kalibro.service.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.SortedSet;

import org.kalibro.DtoTestCase;
import org.kalibro.core.model.MetricConfigurationFixtures;
import org.kalibro.core.model.Range;

public class RangeXmlTest extends DtoTestCase<Range, RangeXml> {

	@Override
	protected RangeXml newDtoUsingDefaultConstructor() {
		return new RangeXml();
	}

	@Override
	protected Collection<Range> entitiesForTestingConversion() {
		SortedSet<Range> ranges = MetricConfigurationFixtures.configuration("amloc").getRanges();
		return new ArrayList<Range>(ranges);
	}

	@Override
	protected RangeXml createDto(Range range) {
		return new RangeXml(range);
	}
}