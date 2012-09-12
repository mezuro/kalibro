package org.kalibro.client;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;
import org.kalibro.ReadingGroup;
import org.kalibro.service.ReadingGroupEndpoint;
import org.kalibro.service.xml.ReadingGroupXmlRequest;
import org.kalibro.service.xml.ReadingGroupXmlResponse;

public class ReadingGroupClientDaoTest extends ClientTest<// @formatter:off
	ReadingGroup, ReadingGroupXmlRequest, ReadingGroupXmlResponse,
	ReadingGroupEndpoint, ReadingGroupClientDao> {// @formatter:on

	@Override
	protected Class<?>[] parameterClasses() {
		return new Class<?>[]{ReadingGroup.class, ReadingGroupXmlRequest.class, ReadingGroupXmlResponse.class,
			ReadingGroupEndpoint.class, ReadingGroupClientDao.class};
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldConfirmExistence() {
		when(port.readingGroupExists(42L)).thenReturn(true);
		assertFalse(client.exists(28L));
		assertTrue(client.exists(42L));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldGetById() {
		when(port.getReadingGroup(42L)).thenReturn(response);
		assertSame(entity, client.get(42L));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldGetAll() {
		when(port.allReadingGroups()).thenReturn(Arrays.asList(response));
		assertDeepList(client.all(), entity);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSave() {
		when(port.saveReadingGroup(request)).thenReturn(42L);
		assertEquals(42L, client.save(entity).longValue());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldDelete() {
		client.delete(42L);
		verify(port).deleteReadingGroup(42L);
	}
}