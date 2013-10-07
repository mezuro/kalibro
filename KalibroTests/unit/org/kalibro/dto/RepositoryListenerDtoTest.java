package org.kalibro.dto;

import org.kalibro.RepositoryListener;

public class RepositoryListenerDtoTest extends AbstractDtoTest<RepositoryListener> {

	@Override
	protected RepositoryListener loadFixture() {
		return new RepositoryListener("RepositoryListenerDtoTest name",
			"RepositoryListenerDtoTest email");
	}

}
