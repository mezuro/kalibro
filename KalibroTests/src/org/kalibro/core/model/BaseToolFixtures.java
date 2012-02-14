package org.kalibro.core.model;

import org.analizo.AnalizoStub;

public class BaseToolFixtures {

	public static BaseTool analizoStub() {
		BaseTool analizoStub = new BaseTool("Analizo");
		analizoStub.setCollectorClass(AnalizoStub.class);
		analizoStub.setSupportedMetrics(NativeMetricFixtures.nativeMetrics());
		return analizoStub;
	}
}