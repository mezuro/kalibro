package org.kalibro.service;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Random;

import org.junit.Test;
import org.kalibro.Range;
import org.kalibro.client.EndpointTest;
import org.kalibro.dao.RangeDao;
import org.kalibro.service.xml.RangeXml;
import org.powermock.reflect.Whitebox;

public class RangeEndpointTest extends EndpointTest<Range, RangeDao, RangeEndpoint> {

	private static final Long ID = new Random().nextLong();
	private static final Long METRIC_CONFIGURATION_ID = new Random().nextLong();

	@Override
	public Range loadFixture() {
		Range range = loadFixture("lcom4-bad", Range.class);
		Whitebox.setInternalState(range, "id", ID);
		Whitebox.setInternalState(range.getReading(), "id", ID);
		return range;
	}

	@Override
	public List<String> fieldsThatShouldBeProxy() {
		return list("reading");
	}

	@Test
	public void shouldGetRangesOfMetricConfiguration() {
		when(dao.rangesOf(METRIC_CONFIGURATION_ID)).thenReturn(sortedSet(entity));
		assertDeepDtoList(list(entity), port.rangesOf(METRIC_CONFIGURATION_ID));
	}

	@Test
	public void shouldSave() {
		when(dao.save(entity, METRIC_CONFIGURATION_ID)).thenReturn(ID);
		assertEquals(ID, port.saveRange(new RangeXml(entity), METRIC_CONFIGURATION_ID));
	}

	@Test
	public void shouldDelete() {
		port.deleteRange(ID);
		verify(dao).delete(ID);
	}
}