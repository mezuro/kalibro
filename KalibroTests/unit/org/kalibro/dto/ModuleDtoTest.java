package org.kalibro.dto;

import org.kalibro.Granularity;
import org.kalibro.Module;

public class ModuleDtoTest extends AbstractDtoTest<Module> {

	@Override
	protected Module loadFixture() {
		return new Module(Granularity.METHOD, "org", "kalibro", "dto", "ModuleDtoTest", "loadFixture");
	}
}