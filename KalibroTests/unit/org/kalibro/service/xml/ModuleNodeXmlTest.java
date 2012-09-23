package org.kalibro.service.xml;

import static org.kalibro.core.model.ModuleNodeFixtures.analizoCheckstyleTree;

import org.kalibro.core.model.ModuleNode;

public class ModuleNodeXmlTest extends XmlTest<ModuleNode> {

	@Override
	protected ModuleNode loadFixture() {
		return analizoCheckstyleTree();
	}
}