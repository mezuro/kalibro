package org.kalibro.service.xml;

public class ModuleResultXmlTest extends XmlTest {

	@Override
	protected void verifyElements() {
		assertElement("id", Long.class);
		assertElement("module", ModuleXml.class);
		assertElement("grade", Double.class);
	}
}