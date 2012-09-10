package org.kalibro.service;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.ReadingGroup;
import org.kalibro.TestCase;
import org.kalibro.core.dao.DaoFactory;
import org.kalibro.core.dao.ReadingGroupDao;
import org.kalibro.service.xml.ReadingGroupXmlRequest;
import org.kalibro.service.xml.ReadingGroupXmlResponse;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({DaoFactory.class, ReadingGroupEndpointImpl.class})
public class ReadingGroupEndpointImplTest extends TestCase {

	private ReadingGroup group;
	private ReadingGroupXmlRequest groupRequest;
	private ReadingGroupXmlResponse groupResponse;

	private ReadingGroupDao dao;
	private ReadingGroupEndpointImpl endpoint;

	@Before
	public void setUp() throws Exception {
		mockReadingGroup();
		mockReadingGroupDao();
		endpoint = new ReadingGroupEndpointImpl();
	}

	private void mockReadingGroup() throws Exception {
		group = mock(ReadingGroup.class);
		groupRequest = mock(ReadingGroupXmlRequest.class);
		groupResponse = mock(ReadingGroupXmlResponse.class);
		when(groupRequest.convert()).thenReturn(group);
		whenNew(ReadingGroupXmlResponse.class).withArguments(group).thenReturn(groupResponse);
	}

	private void mockReadingGroupDao() {
		dao = mock(ReadingGroupDao.class);
		mockStatic(DaoFactory.class);
		when(DaoFactory.getReadingGroupDao()).thenReturn(dao);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldGetAllReadingGroups() {
		when(dao.all()).thenReturn(Arrays.asList(group));
		assertDeepList(endpoint.all(), groupResponse);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSaveReadingGroup() {
		when(group.getId()).thenReturn(42L);
		assertEquals(42L, endpoint.save(groupRequest).longValue());
		verify(dao).save(group);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldDeleteReadingGroup() {
		endpoint.delete(42L);
		verify(dao).delete(42L);
	}
}