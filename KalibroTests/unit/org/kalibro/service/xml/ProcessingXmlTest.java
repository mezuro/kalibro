package org.kalibro.service.xml;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;
import org.kalibro.ModuleResult;
import org.kalibro.ProcessState;
import org.kalibro.Processing;

public class ProcessingXmlTest extends XmlTest {

	@Override
	protected void verifyElements() {
		assertElement("id", Long.class, true);
		assertElement("date", Date.class, true);
		assertElement("state", ProcessState.class, true);
		assertElement("error", ThrowableXml.class);
		assertCollection("processTime");
		assertElement("resultsRootId", Long.class);
	}

	@Test
	public void shouldRetrieveResultsRootId() {
		ModuleResult resultsRoot = mock(ModuleResult.class);
		when(resultsRoot.getId()).thenReturn(42L);
		Processing processing = (Processing) entity;
		processing.setResultsRoot(resultsRoot);

		assertEquals(42L, new ProcessingXml(processing).resultsRootId().longValue());
	}

	@Test
	public void checkNullError() {
		assertNull(new ProcessingXml().error());
	}
}