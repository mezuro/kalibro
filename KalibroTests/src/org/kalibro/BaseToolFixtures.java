package org.kalibro;

import org.analizo.AnalizoMetricCollector;
import org.analizo.AnalizoStub;

@Deprecated
public final class BaseToolFixtures {

	private static BaseTool analizo = newAnalizo();
	private static BaseTool analizoStub = newAnalizoStub();

	public static BaseTool analizo() {
		return analizo;
	}

	public static BaseTool newAnalizo() {
		return newAnalizoBaseTool(AnalizoMetricCollector.class);
	}

	public static BaseTool analizoStub() {
		return analizoStub;
	}

	public static BaseTool newAnalizoStub() {
		return newAnalizoBaseTool(AnalizoStub.class);
	}

	private static BaseTool newAnalizoBaseTool(Class<? extends MetricCollector> collectorClass) {
		return new BaseTool(collectorClass.getName());
	}

	private BaseToolFixtures() {
		return;
	}
}