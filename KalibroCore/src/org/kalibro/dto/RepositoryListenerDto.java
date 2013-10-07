package org.kalibro.dto;

import org.kalibro.RepositoryListener;

/**
 * Data transfer object for {@link RepositoryListener}.
 * 
 * @author Daniel Alves
 * @author Diego Araújo
 * @author Guilherme Rojas
 */
public abstract class RepositoryListenerDto extends DataTransferObject<RepositoryListener> {

	@Override
	public RepositoryListener convert() {
		RepositoryListener repositoryListener = new RepositoryListener();
		setId(repositoryListener, id());
		repositoryListener.setName(name());
		repositoryListener.setEmail(email());
		return repositoryListener;
	}

	public abstract Long id();

	public abstract String name();

	public abstract String email();
}
