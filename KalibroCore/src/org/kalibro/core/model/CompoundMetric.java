package org.kalibro.core.model;

import org.kalibro.core.model.enums.Granularity;

public class CompoundMetric extends Metric {

	private String script;

	public CompoundMetric() {
		super(true, "New metric", Granularity.CLASS);
		setScript("return 1;");
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}
}