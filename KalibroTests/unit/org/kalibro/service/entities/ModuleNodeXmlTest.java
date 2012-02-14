package org.kalibro.service.entities;

import static org.kalibro.core.model.ModuleNodeFixtures.*;

import java.util.Arrays;
import java.util.Collection;

import org.kalibro.DtoTestCase;
import org.kalibro.core.model.ModuleNode;

public class ModuleNodeXmlTest extends DtoTestCase<ModuleNode, ModuleNodeXml> {

	@Override
	protected ModuleNodeXml newDtoUsingDefaultConstructor() {
		return new ModuleNodeXml();
	}

	@Override
	protected Collection<ModuleNode> entitiesForTestingConversion() {
		return Arrays.asList(helloWorldTree(), junitAnalizoTree());
	}

	@Override
	protected ModuleNodeXml createDto(ModuleNode moduleNode) {
		return new ModuleNodeXml(moduleNode);
	}
}