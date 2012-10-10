package org.kalibro.service.xml;

public class ModuleResultXmlTest extends XmlTest {

	@Override
	public void verifyElements() {
		assertElement("id", Long.class);
		assertElement("module", ModuleXml.class);
		assertElement("grade", Double.class);
	}
}