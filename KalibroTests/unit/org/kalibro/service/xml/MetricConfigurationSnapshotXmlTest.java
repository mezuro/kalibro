package org.kalibro.service.xml;

import static org.junit.Assert.*;

import org.junit.Test;
import org.kalibro.MetricConfiguration;
import org.kalibro.ReadingGroup;
import org.kalibro.Statistic;
import org.powermock.reflect.Whitebox;

public class MetricConfigurationSnapshotXmlTest extends XmlTest {

	@Override
	public void setUp() throws Exception {
		super.setUp();
		Whitebox.setInternalState(entity, "readingGroup", (ReadingGroup) null);
	}

	@Override
	protected void verifyElements() {
		assertElement("code", String.class);
		assertElement("weight", Double.class);
		assertElement("aggregationForm", Statistic.class);
		assertElement("metric", MetricXmlResponse.class);
		assertElement("baseToolName", String.class);
		assertCollection("range");
	}

	@Test
	public void shouldConvertNullRangesIntoEmptyCollection() {
		assertTrue(new MetricConfigurationSnapshotXml().ranges().isEmpty());
	}

	@Test
	public void shouldNotPreserveIdNorReadingGroup() {
		MetricConfiguration metricConfiguration = new MetricConfiguration();
		Whitebox.setInternalState(metricConfiguration, "id", 42L);
		metricConfiguration.setReadingGroup(loadFixture("scholar", ReadingGroup.class));
		MetricConfigurationSnapshotXml snapshot = new MetricConfigurationSnapshotXml(metricConfiguration);
		assertNull(snapshot.id());
		assertNull(snapshot.readingGroup());
	}

	@Test
	public void shouldPreserveBaseToolName() {
		MetricConfiguration metricConfiguration = loadFixture("lcom4", MetricConfiguration.class);
		String baseToolName = metricConfiguration.getBaseTool().getName();
		assertEquals(baseToolName, new MetricConfigurationSnapshotXml(metricConfiguration).baseToolName());
	}
}