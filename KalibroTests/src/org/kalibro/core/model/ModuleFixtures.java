package org.kalibro.core.model;

import org.kalibro.core.model.enums.Granularity;

public class ModuleFixtures {

	public static Module helloWorldClass() {
		return new Module(Granularity.CLASS, "HelloWorld");
	}

	public static Module helloWorldApplication() {
		return new Module(Granularity.APPLICATION, "HelloWorld-1.0");
	}
}