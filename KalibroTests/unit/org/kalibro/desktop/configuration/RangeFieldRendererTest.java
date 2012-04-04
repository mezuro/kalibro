package org.kalibro.desktop.configuration;

import static org.junit.Assert.*;
import static org.kalibro.core.model.MetricConfigurationFixtures.*;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.model.Range;

public class RangeFieldRendererTest extends KalibroTestCase {

	private Set<Range> ranges;
	private RangeFieldRenderer renderer;

	@Before
	public void setUp() {
		ranges = metricConfiguration("amloc").getRanges();
		renderer = new RangeFieldRenderer();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRenderStringWithRangeColorAsBackground() {
		for (Range range : ranges)
			assertEquals(range.getColor(), renderer.render("Label", range).getBackground());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRenderDoubleWithRangeColorAsBackground() {
		for (Range range : ranges)
			assertEquals(range.getColor(), renderer.render(42.0, range).getBackground());
	}
}