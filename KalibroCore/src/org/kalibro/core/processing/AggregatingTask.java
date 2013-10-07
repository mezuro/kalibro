package org.kalibro.core.processing;

/**
 * Runs results aggregation procedure.
 * 
 * @author Carlos Morais
 */
class AggregatingTask extends ProcessSubtask {

	AggregatingTask(ProcessContext context) {
		super(context);
	}

	@Override
	protected void perform() {
		context.moduleResultDao().aggregateResults(context.processing().getId());
	}
}