package org.kalibro.dto;

import org.kalibro.ProcessingObserver;

public abstract class ProcessingObserverDto extends DataTransferObject<ProcessingObserver> {

	@Override
	public ProcessingObserver convert() {
		ProcessingObserver processingObserver = new ProcessingObserver();
		setId(processingObserver, id());
		processingObserver.setName(name());
		processingObserver.setEmail(email());
		return processingObserver;
	}

	public abstract Long id();

	public abstract String name();

	public abstract String email();
}
