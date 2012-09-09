package org.kalibro.service.entities;

import static org.junit.Assert.assertEquals;
import static org.kalibro.core.model.MetricConfigurationFixtures.metricConfiguration;
import static org.kalibro.core.model.RangeFixtures.newRange;
import static org.kalibro.core.model.RangeLabel.GOOD;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.SortedSet;

import org.junit.Test;
import org.kalibro.DtoTestCase;
import org.kalibro.core.model.Range;
import org.powermock.reflect.Whitebox;

public class RangeXmlTest extends DtoTestCase<Range, RangeXml> {

	@Override
	protected RangeXml newDtoUsingDefaultConstructor() {
		return new RangeXml();
	}

	@Override
	protected Collection<Range> entitiesForTestingConversion() {
		SortedSet<Range> ranges = metricConfiguration("amloc").getRanges();
		return new ArrayList<Range>(ranges);
	}

	@Override
	protected RangeXml createDto(Range range) {
		return new RangeXml(range);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldTurnNullUnrequiredValuesToDefault() {
		Range range = newRange("amloc", GOOD);
		RangeXml dto = createDto(range);
		Whitebox.setInternalState(dto, "color", (Object) null);
		Whitebox.setInternalState(dto, "comments", (Object) null);
		Range converted = dto.convert();
		assertEquals(Color.WHITE, converted.getColor());
		assertEquals("", converted.getComments());
	}
}