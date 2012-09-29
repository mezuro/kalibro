package org.kalibro.service.xml;

import static org.kalibro.ModuleFixtures.helloWorldApplication;

import org.kalibro.Module;

public class ModuleXmlTest extends XmlTest<Module> {

	@Override
	protected Module loadFixture() {
		return helloWorldApplication();
	}
}