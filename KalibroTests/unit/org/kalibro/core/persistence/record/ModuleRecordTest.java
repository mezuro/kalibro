package org.kalibro.core.persistence.record;

import org.kalibro.core.model.Module;
import org.kalibro.core.model.ModuleFixtures;

public class ModuleRecordTest extends RecordTest<Module> {

	@Override
	protected Module loadFixture() {
		return ModuleFixtures.helloWorldClass();
	}
}