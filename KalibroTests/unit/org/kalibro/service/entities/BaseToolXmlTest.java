package org.kalibro.service.entities;

import static org.kalibro.core.model.BaseToolFixtures.analizoStub;

import org.kalibro.core.model.BaseTool;
import org.kalibro.service.xml.XmlTest;

public class BaseToolXmlTest extends XmlTest<BaseTool> {

	@Override
	protected BaseTool loadFixture() {
		return analizoStub();
	}
}