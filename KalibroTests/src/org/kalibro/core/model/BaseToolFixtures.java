package org.kalibro.core.model;

import org.analizo.AnalizoStub;

public final class BaseToolFixtures {

	public static BaseTool analizoStub() {
		BaseTool analizoStub = new BaseTool("Analizo");
		analizoStub.setCollectorClass(AnalizoStub.class);
		analizoStub.setSupportedMetrics(NativeMetricFixtures.nativeMetrics());
		return analizoStub;
	}

	private BaseToolFixtures() {
		// Utility class
	}
}