package org.kalibro;

/**
 * Contains all {@link NativeMetricResult}s of a {@link Module}.
 * 
 * @author Carlos Morais
 */
public class NativeModuleResult extends AbstractModuleResult<NativeMetricResult> {

	@SuppressWarnings("unused" /* used by SnakeYaml */)
	private NativeModuleResult() {
		this(new Module(Granularity.CLASS));
	}

	/**
	 * Creates a new NativeModuleResult with no metric results.
	 * 
	 * @param module The module this result refers.
	 */
	public NativeModuleResult(Module module) {
		super(module);
	}
}