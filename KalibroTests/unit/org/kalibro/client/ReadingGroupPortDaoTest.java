package org.kalibro.client;

import static org.junit.Assert.assertSame;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.ReadingGroup;
import org.kalibro.TestCase;
import org.kalibro.service.ReadingGroupEndpoint;
import org.kalibro.service.xml.ReadingGroupXmlRequest;
import org.kalibro.service.xml.ReadingGroupXmlResponse;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ReadingGroupClientDao.class, EndpointClient.class})
public class ReadingGroupPortDaoTest extends TestCase {

	private ReadingGroup group;
	private ReadingGroupXmlRequest groupRequest;
	private ReadingGroupXmlResponse groupResponse;

	private ReadingGroupClientDao dao;
	private ReadingGroupEndpoint port;

	@Before
	public void setUp() throws Exception {
		mockReadingGroup();
		createSupressedDao();
	}

	private void mockReadingGroup() throws Exception {
		group = mock(ReadingGroup.class);
		groupRequest = mock(ReadingGroupXmlRequest.class);
		groupResponse = mock(ReadingGroupXmlResponse.class);
		when(groupResponse.convert()).thenReturn(group);
		whenNew(ReadingGroupXmlRequest.class).withArguments(group).thenReturn(groupRequest);
	}

	private void createSupressedDao() {
		suppress(constructor(EndpointClient.class, String.class, Class.class));
		dao = new ReadingGroupClientDao("");

		port = mock(ReadingGroupEndpoint.class);
		Whitebox.setInternalState(dao, "port", port);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldGetAllReadingGroups() {
		when(port.all()).thenReturn(Arrays.asList(groupResponse));
		assertDeepList(dao.all(), group);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldGetById() {
		when(port.get(42L)).thenReturn(groupResponse);
		assertSame(group, dao.get(42L));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSaveAndMerge() {
		when(port.save(groupRequest)).thenReturn(42L);
		dao.save(group);
		verify(group).setId(42L);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldDeleteReadingGroup() {
		dao.delete(42L);
		verify(port).delete(42L);
	}
}