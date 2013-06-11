package org.kalibro.dto;

import org.kalibro.ProcessingObserver;

public class ProcessingObserverDtoTest extends AbstractDtoTest<ProcessingObserver> {

	@Override
	protected ProcessingObserver loadFixture() {
		return new ProcessingObserver("ProcessingObserverDtoTest name",
			"ProcessingObserverDtoTest email");
	}

}
