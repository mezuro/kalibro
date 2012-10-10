package org.kalibro.service.xml;

import java.util.Date;

import org.kalibro.ProcessState;

public class ProcessingXmlTest extends XmlTest {

	@Override
	public void verifyElements() {
		assertElement("id", Long.class);
		assertElement("date", Date.class);
		assertElement("state", ProcessState.class);
		assertElement("error", ThrowableXml.class);
		assertCollection("stateTime");
	}
}