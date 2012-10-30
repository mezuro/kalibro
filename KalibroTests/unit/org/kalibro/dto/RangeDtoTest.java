package org.kalibro.dto;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.kalibro.Range;

public class RangeDtoTest extends AbstractDtoTest<Range> {

	@Override
	protected Range loadFixture() {
		return loadFixture("lcom4-bad", Range.class);
	}

	@Test
	public void shouldConvertNullCommentsIntoEmptyString() throws Exception {
		when(dto, "comments").thenReturn(null);
		assertEquals("", dto.convert().getComments());
	}
}