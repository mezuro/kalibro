package org.kalibro.core.model;

import static org.kalibro.core.model.MetricFixtures.*;

import org.analizo.AnalizoMetricCollector;
import org.analizo.AnalizoStub;
import org.kalibro.BaseTool;
import org.kalibro.core.MetricCollector;

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
		BaseTool baseTool = new BaseTool("Analizo");
		baseTool.setCollectorClass(collectorClass);
		for (String code : analizoMetricCodes())
			baseTool.addSupportedMetric(newAnalizoMetric(code));
		return baseTool;
	}

	private BaseToolFixtures() {
		return;
	}
}