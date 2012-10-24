package org.kalibro.service;

import static org.junit.Assert.assertEquals;

import java.util.Random;

import org.junit.Test;
import org.kalibro.Range;
import org.kalibro.dao.RangeDao;
import org.kalibro.service.xml.RangeXmlRequest;
import org.kalibro.service.xml.RangeXmlResponse;
import org.powermock.core.classloader.annotations.PrepareForTest;

@PrepareForTest(RangeEndpointImpl.class)
public class RangeEndpointImplTest extends
	EndpointImplementorTest<Range, RangeXmlRequest, RangeXmlResponse, RangeDao, RangeEndpointImpl> {

	private static final Long ID = new Random().nextLong();
	private static final Long METRIC_CONFIGURATION_ID = new Random().nextLong();

	@Override
	protected Class<Range> entityClass() {
		return Range.class;
	}

	@Test
	public void shouldGetRangesOfGroup() {
		when(dao.rangesOf(METRIC_CONFIGURATION_ID)).thenReturn(sortedSet(entity));
		assertDeepEquals(list(response), implementor.rangesOf(METRIC_CONFIGURATION_ID));
	}

	@Test
	public void shouldSave() {
		when(dao.save(entity, METRIC_CONFIGURATION_ID)).thenReturn(ID);
		assertEquals(ID, implementor.saveRange(request, METRIC_CONFIGURATION_ID));
	}

	@Test
	public void shouldDeleteRange() {
		implementor.deleteRange(ID);
		verify(dao).delete(ID);
	}
}