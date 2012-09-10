package org.kalibro.dto;

import java.util.Arrays;

import org.junit.Test;
import org.kalibro.Reading;

public class ReadingDtoTest extends AbstractDtoTest<Reading, ReadingDto> {

	@Override
	protected Reading loadFixture() {
		return loadFixture("/org/kalibro/reading-excellent", Reading.class);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldConvertList() {
		assertDeepList(ReadingDto.convert(Arrays.asList(dto)), entity);
	}
}