package org.kalibro.service.xml;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.kalibro.BaseTool;
import org.kalibro.ReadingGroup;
import org.kalibro.Statistic;
import org.powermock.reflect.Whitebox;

public class MetricConfigurationSnapshotXmlTest extends XmlTest {

	@Override
	public void setUp() throws Exception {
		super.setUp();
		Whitebox.setInternalState(entity, "baseTool", (BaseTool) null);
		Whitebox.setInternalState(entity, "readingGroup", (ReadingGroup) null);
	}

	@Override
	protected void verifyElements() {
		assertElement("code", String.class);
		assertElement("metric", MetricXmlResponse.class);
		assertElement("weight", Double.class);
		assertElement("aggregationForm", Statistic.class);
		assertCollection("range");
	}

	@Test
	public void shouldConvertNullRangesIntoEmptyCollection() {
		assertTrue(new MetricConfigurationSnapshotXml().ranges().isEmpty());
	}
}