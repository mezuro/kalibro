package org.kalibro.dto;

import org.kalibro.Reading;

public class ReadingDtoTest extends AbstractDtoTest<Reading> {

	@Override
	protected Reading loadFixture() {
		return loadFixture("excellent", Reading.class);
	}
}