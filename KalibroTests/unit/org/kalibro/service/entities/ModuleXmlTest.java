package org.kalibro.service.entities;

import static org.kalibro.core.model.ModuleFixtures.*;

import java.util.Arrays;
import java.util.Collection;

import org.kalibro.DtoTestCase;
import org.kalibro.core.model.Module;

public class ModuleXmlTest extends DtoTestCase<Module, ModuleXml> {

	@Override
	protected ModuleXml newDtoUsingDefaultConstructor() {
		return new ModuleXml();
	}

	@Override
	protected Collection<Module> entitiesForTestingConversion() {
		return Arrays.asList(helloWorldApplication(), helloWorldClass());
	}

	@Override
	protected ModuleXml createDto(Module module) {
		return new ModuleXml(module);
	}
}