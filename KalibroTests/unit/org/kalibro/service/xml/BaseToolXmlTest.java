package org.kalibro.service.xml;

import static org.kalibro.core.model.BaseToolFixtures.analizoStub;

import org.kalibro.core.model.BaseTool;

public class BaseToolXmlTest extends XmlTest<BaseTool> {

	@Override
	protected BaseTool loadFixture() {
		return analizoStub();
	}
}