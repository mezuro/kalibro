package org.kalibro.desktop.configuration;

import static org.junit.Assert.assertEquals;
import static org.kalibro.core.model.MetricConfigurationFixtures.metricConfiguration;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.core.model.Range;
import org.kalibro.tests.UnitTest;

public class RangeFieldRendererTest extends UnitTest {

	private Set<Range> ranges;
	private RangeFieldRenderer renderer;

	@Before
	public void setUp() {
		ranges = metricConfiguration("amloc").getRanges();
		renderer = new RangeFieldRenderer();
	}

	@Test
	public void shouldRenderStringWithRangeColorAsBackground() {
		for (Range range : ranges)
			assertEquals(range.getColor(), renderer.render("Label", range).getBackground());
	}

	@Test
	public void shouldRenderDoubleWithRangeColorAsBackground() {
		for (Range range : ranges)
			assertEquals(range.getColor(), renderer.render(42.0, range).getBackground());
	}
}