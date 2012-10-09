package org.kalibro.dto;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.kalibro.BaseTool;

public class BaseToolDtoTest extends AbstractDtoTest<BaseTool> {

	@Override
	protected BaseTool loadFixture() {
		return loadFixture("inexistent", BaseTool.class);
	}

	@Test
	public void shouldConvertNullDescriptionIntoEmptyString() throws Exception {
		when(dto, "description").thenReturn(null);
		assertEquals("", dto.convert().getDescription());
	}
}