package org.kalibro.service.xml;

import static org.kalibro.ModuleNodeFixtures.analizoCheckstyleTree;

import org.kalibro.ModuleNode;

public class ModuleNodeXmlTest extends XmlTest<ModuleNode> {

	@Override
	protected ModuleNode loadFixture() {
		return analizoCheckstyleTree();
	}
}