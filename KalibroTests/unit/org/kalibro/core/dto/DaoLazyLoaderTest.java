package org.kalibro.core.dto;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.powermock.api.mockito.PowerMockito.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.Reading;
import org.kalibro.TestCase;
import org.kalibro.core.dao.DaoFactory;
import org.kalibro.core.dao.ReadingDao;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DaoFactory.class)
public class DaoLazyLoaderTest extends TestCase {

	private ReadingDao dao;
	private List<Reading> real, proxy;

	@Before
	public void setUp() {
		dao = mock(ReadingDao.class);
		mockStatic(DaoFactory.class);
		when(DaoFactory.getReadingDao()).thenReturn(dao);
		real = mock(List.class);
		when(dao.readingsOf(42L)).thenReturn(real);
		proxy = (List<Reading>) DaoLazyLoader.createProxy(ReadingDao.class, "readingsOf", 42L);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldLoadOnFirstInvocation() {
		verifyStatic(never());
		DaoFactory.getReadingDao();
		verify(dao, never()).readingsOf(any(Long.class));

		proxy.size();
		verifyStatic();
		DaoFactory.getReadingDao();
		verify(dao).readingsOf(any(Long.class));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldFetchOnlyOnce() {
		proxy.size();
		proxy.toArray();
		proxy.toString();
		verify(dao, once()).readingsOf(any(Long.class));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void proxyShouldActAsRealObject() {
		when(real.size()).thenReturn(42);
		assertEquals(42, proxy.size());

		Reading reading = mock(Reading.class);
		when(real.get(42)).thenReturn(reading);
		assertSame(reading, proxy.get(42));
	}
}