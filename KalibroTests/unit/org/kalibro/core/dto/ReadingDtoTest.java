package org.kalibro.core.dto;

import org.kalibro.Reading;

public class ReadingDtoTest extends AbstractDtoTest<Reading, ReadingDto> {

	@Override
	protected Reading loadFixture() {
		return loadFixture("/org/kalibro/reading-excellent", Reading.class);
	}
}