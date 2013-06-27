package org.kalibro.dto;

import org.kalibro.RepositoryObserver;

/**
 * Data transfer object for {@link RepositoryObserver}.
 * 
 * @author Daniel Alves
 * @author Diego Araújo
 * @author Guilherme Rojas
 */
public abstract class RepositoryObserverDto extends DataTransferObject<RepositoryObserver> {

	@Override
	public RepositoryObserver convert() {
		RepositoryObserver repositoryObserver = new RepositoryObserver();
		setId(repositoryObserver, id());
		repositoryObserver.setName(name());
		repositoryObserver.setEmail(email());
		return repositoryObserver;
	}

	public abstract Long id();

	public abstract String name();

	public abstract String email();
}
