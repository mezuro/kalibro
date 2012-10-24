package org.kalibro.dto;

import static org.junit.Assert.assertSame;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.Reading;
import org.kalibro.dao.DaoFactory;
import org.kalibro.dao.ReadingDao;
import org.kalibro.tests.UnitTest;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DaoFactory.class)
public class DaoLazyLoaderTest extends UnitTest {

	private static final Long ID = new Random().nextLong();

	private ReadingDao dao;
	private Reading real, proxy;

	@Before
	public void setUp() {
		real = loadFixture("excellent", Reading.class);
		mockReadingDao();
		proxy = DaoLazyLoader.createProxy(ReadingDao.class, "get", ID);
	}

	private void mockReadingDao() {
		dao = mock(ReadingDao.class);
		mockStatic(DaoFactory.class);
		when(DaoFactory.getReadingDao()).thenReturn(dao);
		when(dao.get(ID)).thenReturn(real);
	}

	@Test
	public void shouldLoadOnFirstInvocation() {
		verifyStatic(never());
		DaoFactory.getReadingDao();

		proxy.getLabel();
		verifyStatic();
		DaoFactory.getReadingDao();
		verify(dao).get(ID);
	}

	@Test
	public void shouldFetchOnlyOnce() {
		proxy.getLabel();
		proxy.getGrade();
		proxy.getColor();
		verify(dao, once()).get(ID);
	}

	@Test
	public void proxyShouldActAsRealObject() {
		assertSame(real.getLabel(), proxy.getLabel());
		assertSame(real.getGrade(), proxy.getGrade());
		assertSame(real.getColor(), proxy.getColor());
	}
}