package org.kalibro.client;

import static org.junit.Assert.assertEquals;

import java.util.Random;

import org.junit.Test;
import org.kalibro.Range;
import org.kalibro.service.RangeEndpoint;
import org.kalibro.service.xml.RangeXml;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;

@PrepareOnlyThisForTest(RangeClientDao.class)
public class RangeClientDaoTest extends ClientTest<Range, RangeXml, RangeXml, RangeEndpoint, RangeClientDao> {

	private static final Long ID = new Random().nextLong();
	private static final Long METRIC_CONFIGURATION_ID = new Random().nextLong();

	@Override
	protected Class<Range> entityClass() {
		return Range.class;
	}

	@Test
	public void shouldGetRangesOfMetricConfiguration() {
		when(port.rangesOf(METRIC_CONFIGURATION_ID)).thenReturn(list(response));
		assertDeepEquals(set(entity), client.rangesOf(METRIC_CONFIGURATION_ID));
	}

	@Test
	public void shouldSave() {
		when(port.saveRange(request, METRIC_CONFIGURATION_ID)).thenReturn(ID);
		assertEquals(ID, client.save(entity, METRIC_CONFIGURATION_ID));
	}

	@Test
	public void shouldDelete() {
		client.delete(ID);
		verify(port).deleteRange(ID);
	}
}