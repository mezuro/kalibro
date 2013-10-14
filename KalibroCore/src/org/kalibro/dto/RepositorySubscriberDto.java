package org.kalibro.dto;

import org.kalibro.RepositorySubscriber;

/**
 * Data transfer object for {@link RepositorySubscriber}.
 * 
 * @author Daniel Alves
 * @author Diego Araújo
 * @author Guilherme Rojas
 */
public abstract class RepositorySubscriberDto extends DataTransferObject<RepositorySubscriber> {

	@Override
	public RepositorySubscriber convert() {
		RepositorySubscriber repositorySubscriber = new RepositorySubscriber();
		setId(repositorySubscriber, id());
		repositorySubscriber.setName(name());
		repositorySubscriber.setEmail(email());
		return repositorySubscriber;
	}

	public abstract Long id();

	public abstract String name();

	public abstract String email();
}
