package org.kalibro.service.xml;

import org.kalibro.Granularity;

public class ModuleXmlTest extends XmlTest {

	@Override
	public void verifyElements() {
		assertElement("name", String[].class);
		assertElement("granularity", Granularity.class);
	}
}