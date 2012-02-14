package org.kalibro.core.model;

public class CompoundMetricFixtures {

	public static CompoundMetric sc() {
		CompoundMetric sc = new CompoundMetric();
		sc.setName("Structural complexity");
		sc.setScript("return cbo * lcom4;");
		return sc;
	}
}