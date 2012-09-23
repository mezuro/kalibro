package org.kalibro.service.xml;

import static org.kalibro.core.model.ModuleFixtures.helloWorldApplication;

import org.kalibro.core.model.Module;

public class ModuleXmlTest extends XmlTest<Module> {

	@Override
	protected Module loadFixture() {
		return helloWorldApplication();
	}
}