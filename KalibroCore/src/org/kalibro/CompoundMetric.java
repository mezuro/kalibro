package org.kalibro;

public class CompoundMetric extends Metric {

	private String script;

	public CompoundMetric() {
		this("New metric");
	}

	public CompoundMetric(String name) {
		super(true, name, Granularity.CLASS);
		setScript("return 1;");
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}
}