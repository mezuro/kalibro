package org.kalibro.service.xml;

import org.kalibro.Granularity;

public class ModuleXmlTest extends XmlTest {

	@Override
	protected void verifyElements() {
		assertElement("name", String[].class);
		assertElement("granularity", Granularity.class);
	}
}