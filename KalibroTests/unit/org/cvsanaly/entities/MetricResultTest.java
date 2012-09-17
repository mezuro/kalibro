package org.cvsanaly.entities;

import static org.junit.Assert.assertEquals;

import org.cvsanaly.DataObjectTest;
import org.junit.Test;

public class MetricResultTest extends DataObjectTest<MetricResult> {

	@Override
	protected Class<MetricResult> getTypeClass() {
		return MetricResult.class;
	}

	@Test
	public void testConstructorWithFileLink() throws IllegalAccessException {
		MetricResult metricResult = new MetricResult();
		metricResult.setId(42);
		metricResult.setNumberOfBlankLines(43);
		metricResult.setHalsteadVolume(44);
		FileLink fileLink = new FileLink();
		fileLink.setId(1);
		fileLink.setFilePath("aaa/bbb");
		
		MetricResult actual = new MetricResult(metricResult, fileLink);
		assertEquals(fileLink.getFilePath(), actual.getFilePath());
		assertEquals(metricResult.getId(), actual.getId());
		assertDoubleEquals(metricResult.getNumberOfBlankLines(), actual.getNumberOfBlankLines());
		assertDoubleEquals(metricResult.getHalsteadVolume(), actual.getHalsteadVolume());
	}

}
