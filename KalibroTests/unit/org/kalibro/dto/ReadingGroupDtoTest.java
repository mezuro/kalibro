package org.kalibro.dto;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.kalibro.ReadingGroup;
import org.kalibro.dao.ReadingDao;

public class ReadingGroupDtoTest extends AbstractDtoTest<ReadingGroup> {

	@Override
	protected ReadingGroup loadFixture() {
		return loadFixture("scholar", ReadingGroup.class);
	}

	@Override
	protected void registerLazyLoadExpectations() {
		whenLazy(ReadingDao.class, "readingsOf", entity.getId()).thenReturn(entity.getReadings());
	}

	@Test
	public void shouldConvertNullDescriptionIntoEmptyString() throws Exception {
		when(dto, "description").thenReturn(null);
		assertEquals("", dto.convert().getDescription());
	}
}