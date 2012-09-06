package org.kalibro.core.dto;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.Reading;
import org.kalibro.TestCase;

public class ReadingDtoTest extends TestCase {

	private Reading reading;
	private ReadingDto dto;

	@Before
	public void setUp() {
		reading = loadFixture("/org/kalibro/reading-excellent", Reading.class);
		dto = new ReadingDtoStub(reading);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldConvert() {
		assertDeepEquals(reading, dto.convert());
	}
}