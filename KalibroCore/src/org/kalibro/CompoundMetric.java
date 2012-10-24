package org.kalibro;

/**
 * Metric calculated based on the results of other metrics, using a script.
 * 
 * @author Carlos Morais.
 */
public class CompoundMetric extends Metric {

	private String script;

	public CompoundMetric() {
		this("New metric");
	}

	public CompoundMetric(String name) {
		super(true, name, Granularity.CLASS);
		setScript("return 1;");
	}

	@Override
	public void setName(String name) {
		super.setName(name);
	}

	@Override
	public void setScope(Granularity scope) {
		super.setScope(scope);
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}
}