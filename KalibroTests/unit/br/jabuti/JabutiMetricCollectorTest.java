package br.jabuti;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.core.model.BaseTool;

public class JabutiMetricCollectorTest {
	
	private JabutiMetricCollector jabuti;
	
	@Before
	public void setUp() throws Exception {
		jabuti = new JabutiMetricCollector();
	}

	@Test
	public void checkBaseTool() {
		BaseTool baseTool = jabuti.getBaseTool();
		assertNotNull(baseTool);
		assertEquals(JabutiMetricCollector.class, baseTool.getCollectorClass());
	}
	
}