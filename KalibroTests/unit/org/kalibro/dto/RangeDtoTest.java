package org.kalibro.dto;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;
import org.kalibro.Range;
import org.kalibro.dao.ReadingDao;

public class RangeDtoTest extends AbstractDtoTest<Range> {

	@Override
	protected Range loadFixture() {
		return loadFixture("lcom4-bad", Range.class);
	}

	@Override
	protected void registerLazyLoadExpectations() throws Exception {
		Long readingId = new Random().nextLong();
		doReturn(readingId).when(dto, "readingId");
		whenLazy(ReadingDao.class, "get", readingId).thenReturn(entity.getReading());
	}

	@Test
	public void shouldConvertNullCommentsIntoEmptyString() throws Exception {
		when(dto, "comments").thenReturn(null);
		assertEquals("", dto.convert().getComments());
	}

	@Test
	public void readingShouldBeNullForNullReadingId() throws Exception {
		when(dto, "readingId").thenReturn(null);
		assertNull(dto.convert().getReading());
	}
}