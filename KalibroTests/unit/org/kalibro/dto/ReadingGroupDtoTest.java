package org.kalibro.dto;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.kalibro.ReadingGroup;
import org.kalibro.dao.ReadingDao;

public class ReadingGroupDtoTest extends AbstractDtoTest<ReadingGroup> {

	@Override
	protected ReadingGroup loadFixture() {
		return loadFixture("scholar", ReadingGroup.class);
	}

	@Override
	protected List<LazyLoadExpectation> lazyLoadExpectations() {
		return Arrays.asList(expectLazy(entity.getReadings(), ReadingDao.class, "readingsOf", entity.getId()));
	}

	@Test
	public void shouldConvertNullDescriptionIntoEmptyString() throws Exception {
		when(dto, "description").thenReturn(null);
		assertEquals("", dto.convert().getDescription());
	}
}