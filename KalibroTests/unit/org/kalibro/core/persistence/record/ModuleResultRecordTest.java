package org.kalibro.core.persistence.record;

import org.kalibro.Module;
import org.kalibro.ModuleFixtures;

public class ModuleResultRecordTest extends RecordTest<Module> {

	@Override
	protected Module loadFixture() {
		return ModuleFixtures.helloWorldClass();
	}
}