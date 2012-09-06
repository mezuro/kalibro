package org.kalibro.core.dto;

import static org.powermock.api.mockito.PowerMockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.ReadingGroup;
import org.kalibro.TestCase;
import org.kalibro.core.dao.ReadingDao;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DaoLazyLoader.class)
public class ReadingGroupDtoTest extends TestCase {

	private ReadingGroup group;
	private ReadingGroupDto dto;

	@Before
	public void setUp() {
		group = loadFixture("/org/kalibro/readingGroup-scholar", ReadingGroup.class);
		dto = new ReadingGroupDtoStub(group);
		mockStatic(DaoLazyLoader.class);
		when(DaoLazyLoader.createProxy(ReadingDao.class, "readingsOf", group.getId())).thenReturn(group.getReadings());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldConvert() {
		assertDeepEquals(group, dto.convert());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldLazyLoadReadings() {
		dto.convert();
		verifyStatic();
		DaoLazyLoader.createProxy(ReadingDao.class, "readingsOf", group.getId());
	}
}