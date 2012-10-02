package org.kalibro;

/**
 * Contains all {@link NativeMetricResult}s of a {@link Module}.
 * 
 * @author Carlos Morais
 */
public class NativeModuleResult extends AbstractModuleResult<NativeMetricResult> {

	/**
	 * Creates a new NativeModuleResult with no metric results.
	 * 
	 * @param module The module this result refers.
	 */
	public NativeModuleResult(Module module) {
		super(module);
	}
}