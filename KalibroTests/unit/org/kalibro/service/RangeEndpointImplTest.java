package org.kalibro.service;

import static org.junit.Assert.assertEquals;

import java.util.Random;

import org.junit.Test;
import org.kalibro.Range;
import org.kalibro.dao.RangeDao;
import org.kalibro.service.xml.RangeXml;
import org.powermock.core.classloader.annotations.PrepareForTest;

@PrepareForTest(RangeEndpointImpl.class)
public class RangeEndpointImplTest extends EndpointImplementorTest<Range, RangeXml, RangeDao, RangeEndpointImpl> {

	private static final Long ID = new Random().nextLong();
	private static final Long METRIC_CONFIGURATION_ID = new Random().nextLong();

	@Test
	public void shouldGetRangesOfGroup() {
		doReturn(sortedSet(entity)).when(dao).rangesOf(METRIC_CONFIGURATION_ID);
		assertDeepEquals(list(xml), implementor.rangesOf(METRIC_CONFIGURATION_ID));
	}

	@Test
	public void shouldSave() {
		when(dao.save(entity, METRIC_CONFIGURATION_ID)).thenReturn(ID);
		assertEquals(ID, implementor.saveRange(xml, METRIC_CONFIGURATION_ID));
	}

	@Test
	public void shouldDeleteRange() {
		implementor.deleteRange(ID);
		verify(dao).delete(ID);
	}
}