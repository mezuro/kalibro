package org.kalibro.core.model;

/**
 * Contains all {@link NativeMetricResult}s of a {@link Module}.
 * 
 * @author Carlos Morais
 */
public final class NativeModuleResult extends AbstractModuleResult<NativeMetricResult> {

	/**
	 * Creates a new NativeModuleResult with no metric results.
	 * 
	 * @param module The module this result refers.
	 */
	public NativeModuleResult(Module module) {
		super(module);
	}
}