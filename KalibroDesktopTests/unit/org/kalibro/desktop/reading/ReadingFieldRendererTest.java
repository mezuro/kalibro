package org.kalibro.desktop.reading;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.kalibro.Reading;
import org.kalibro.tests.UnitTest;

public class ReadingFieldRendererTest extends UnitTest {

	@Test
	public void shouldRenderWithReadingColorAsBackground() {
		Reading reading = loadFixture("excellent", Reading.class);
		ReadingFieldRenderer renderer = new ReadingFieldRenderer();
		assertEquals(reading.getColor(), renderer.render(reading.getLabel(), reading).getBackground());
		assertEquals(reading.getColor(), renderer.render(reading.getGrade(), reading).getBackground());
	}
}