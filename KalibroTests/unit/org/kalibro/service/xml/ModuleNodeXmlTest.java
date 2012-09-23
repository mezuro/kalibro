package org.kalibro.service.xml;

import static org.junit.Assert.assertTrue;
import static org.kalibro.core.model.ModuleNodeFixtures.*;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.kalibro.DtoTestCase;
import org.kalibro.core.model.ModuleNode;
import org.powermock.reflect.Whitebox;

public class ModuleNodeXmlTest extends DtoTestCase<ModuleNode, ModuleNodeXml> {

	@Override
	protected ModuleNodeXml newDtoUsingDefaultConstructor() {
		return new ModuleNodeXml();
	}

	@Override
	protected Collection<ModuleNode> entitiesForTestingConversion() {
		return Arrays.asList(helloWorldRoot(), analizoCheckstyleTree());
	}

	@Override
	protected ModuleNodeXml createDto(ModuleNode moduleNode) {
		return new ModuleNodeXml(moduleNode);
	}

	@Test
	public void shouldTurnNullChildrenIntoEmpty() {
		ModuleNode node = helloWorldLeaf();
		ModuleNodeXml dto = createDto(node);
		Whitebox.setInternalState(dto, "children", (Object) null);
		assertTrue(dto.convert().getChildren().isEmpty());
	}
}