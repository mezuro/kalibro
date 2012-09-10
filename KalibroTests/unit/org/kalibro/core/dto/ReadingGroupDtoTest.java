package org.kalibro.core.dto;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.kalibro.ReadingGroup;
import org.kalibro.core.dao.ReadingDao;

public class ReadingGroupDtoTest extends AbstractDtoTest<ReadingGroup, ReadingGroupDto> {

	@Override
	protected ReadingGroup loadFixture() {
		return loadFixture("/org/kalibro/readingGroup-scholar", ReadingGroup.class);
	}

	@Override
	protected List<LazyLoadExpectation> lazyLoadExpectations() {
		return Arrays.asList(expectLazy(entity.getReadings(), ReadingDao.class, "readingsOf", entity.getId()));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldConvertList() {
		assertDeepList(ReadingGroupDto.convert(Arrays.asList(dto)), entity);
	}
}