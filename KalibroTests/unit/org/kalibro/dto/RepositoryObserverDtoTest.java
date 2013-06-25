package org.kalibro.dto;

import org.kalibro.RepositoryObserver;

public class RepositoryObserverDtoTest extends AbstractDtoTest<RepositoryObserver> {

	@Override
	protected RepositoryObserver loadFixture() {
		return new RepositoryObserver("RepositoryObserverDtoTest name",
			"RepositoryObserverDtoTest email");
	}

}
