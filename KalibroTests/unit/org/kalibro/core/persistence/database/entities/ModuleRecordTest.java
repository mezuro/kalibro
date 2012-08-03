package org.kalibro.core.persistence.database.entities;

import static org.junit.Assert.*;
import static org.kalibro.core.model.ModuleFixtures.*;
import static org.kalibro.core.model.ModuleNodeFixtures.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.DtoTestCase;
import org.kalibro.core.model.*;

public class ModuleRecordTest extends DtoTestCase<ModuleNode, ModuleRecord> {

	private ProjectResult projectResult;

	@Before
	public void setUp() {
		projectResult = ProjectResultFixtures.helloWorldResult();
	}

	@Override
	protected ModuleRecord newDtoUsingDefaultConstructor() {
		return new ModuleRecord();
	}

	@Override
	protected Collection<ModuleNode> entitiesForTestingConversion() {
		return Arrays.asList(helloWorldRoot(), analizoCheckstyleTree());
	}

	@Override
	protected ModuleRecord createDto(ModuleNode moduleNode) {
		return new ModuleRecord(moduleNode, projectResult);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRetrieveIfIsRoot() {
		assertTrue(createDto(helloWorldRoot()).isRoot());
		assertFalse(new ModuleRecord(helloWorldLeaf(), null, new ModuleRecord()).isRoot());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldConvertIntoModuleResult() {
		Date date = projectResult.getDate();
		Module module = helloWorldClass();
		ModuleNode moduleNode = new ModuleNode(module);
		ModuleRecord record = new ModuleRecord(moduleNode, projectResult);
		assertDeepEquals(new ModuleResult(module, date), record.convertIntoModuleResult());
	}
}