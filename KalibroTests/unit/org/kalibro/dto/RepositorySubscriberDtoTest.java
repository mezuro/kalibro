package org.kalibro.dto;

import org.kalibro.RepositorySubscriber;

public class RepositorySubscriberDtoTest extends AbstractDtoTest<RepositorySubscriber> {

	@Override
	protected RepositorySubscriber loadFixture() {
		return new RepositorySubscriber("RepositorySubscriberDtoTest name",
			"RepositorySubscriberDtoTest email");
	}

}
