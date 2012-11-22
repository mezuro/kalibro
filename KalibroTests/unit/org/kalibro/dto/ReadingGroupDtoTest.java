package org.kalibro.dto;

import static org.junit.Assert.assertEquals;

import java.util.Random;

import org.junit.Test;
import org.kalibro.ReadingGroup;
import org.kalibro.dao.ReadingDao;
import org.powermock.reflect.Whitebox;

public class ReadingGroupDtoTest extends AbstractDtoTest<ReadingGroup> {

	@Override
	protected ReadingGroup loadFixture() {
		ReadingGroup group = loadFixture("scholar", ReadingGroup.class);
		Whitebox.setInternalState(group, "id", new Random().nextLong());
		return group;
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