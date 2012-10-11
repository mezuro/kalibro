package org.kalibro.service.xml;

import static org.junit.Assert.assertNull;

import java.util.Date;

import org.junit.Test;
import org.kalibro.ProcessState;

public class ProcessingXmlTest extends XmlTest {

	@Override
	protected void verifyElements() {
		assertElement("id", Long.class);
		assertElement("date", Date.class);
		assertElement("state", ProcessState.class);
		assertElement("error", ThrowableXml.class);
		assertCollection("stateTime");
	}

	@Test
	public void checkNullError() {
		assertNull(new ProcessingXml().error());
	}
}