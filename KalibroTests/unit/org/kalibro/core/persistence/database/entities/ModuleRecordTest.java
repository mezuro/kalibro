package org.kalibro.core.persistence.database.entities;

import static org.junit.Assert.*;
import static org.kalibro.core.model.ModuleNodeFixtures.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import org.junit.Test;
import org.kalibro.DtoTestCase;
import org.kalibro.core.model.Module;
import org.kalibro.core.model.ModuleFixtures;
import org.kalibro.core.model.ModuleNode;
import org.kalibro.core.model.ModuleResult;
import org.kalibro.core.persistence.database.entities.ModuleRecord;

public class ModuleRecordTest extends DtoTestCase<ModuleNode, ModuleRecord> {

	@Override
	protected ModuleRecord newDtoUsingDefaultConstructor() {
		return new ModuleRecord();
	}

	@Override
	protected Collection<ModuleNode> entitiesForTestingConversion() {
		return Arrays.asList(helloWorldTree(), junitAnalizoTree());
	}

	@Override
	protected ModuleRecord createDto(ModuleNode moduleNode) {
		return new ModuleRecord(moduleNode, "", new Date());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRetrieveIfIsRoot() {
		assertTrue(createDto(helloWorldTree()).isRoot());
		assertFalse(new ModuleRecord(helloWorldNode(), null, new ModuleRecord()).isRoot());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldConvertIntoModuleResult() {
		Date date = new Date();
		Module module = ModuleFixtures.helloWorldClass();
		ModuleNode moduleNode = new ModuleNode(module);
		ModuleRecord record = new ModuleRecord(moduleNode, "", date);
		assertDeepEquals(new ModuleResult(module, date), record.convertIntoModuleResult());
	}
}