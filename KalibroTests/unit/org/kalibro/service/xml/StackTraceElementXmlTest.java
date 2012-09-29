package org.kalibro.service.xml;

public class StackTraceElementXmlTest extends XmlTest<StackTraceElement> {

	@Override
	protected StackTraceElement loadFixture() {
		return new Exception().getStackTrace()[0];
	}
}