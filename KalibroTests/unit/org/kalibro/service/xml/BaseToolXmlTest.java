package org.kalibro.service.xml;

import org.kalibro.BaseTool;

public class BaseToolXmlTest extends XmlTest<BaseTool> {

	@Override
	protected BaseTool loadFixture() {
		return loadFixture("inexistent", BaseTool.class);
	}
}