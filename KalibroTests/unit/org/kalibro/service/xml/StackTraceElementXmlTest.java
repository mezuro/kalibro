package org.kalibro.service.xml;

public class StackTraceElementXmlTest extends XmlTest {

	@Override
	protected void verifyElements() {
		assertElement("declaringClass", String.class);
		assertElement("methodName", String.class);
		assertElement("fileName", String.class);
		assertElement("lineNumber", Integer.class);
	}
}