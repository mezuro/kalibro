package org.kalibro.core.processing;

import org.kalibro.Repository;
import org.kalibro.RepositoryType;
import org.kalibro.core.Identifier;
import org.kalibro.core.loaders.RepositoryLoader;

/**
 * Loads source code from a {@link Repository} previously committed for historic processing.
 * 
 * @author Daniel Alves
 * @author Diego Araújo
 * @author Guilherme Rojas
 */
public class HistoricLoadingTask extends LoadingTask {
	@Override
	protected void perform() throws Exception {
		RepositoryLoader repositoryLoader = createLoader();
		prepareCodeDirectory();
		
		// FIXME: merge methods?
		repositoryLoader.load(repository().getAddress(), codeDirectory());
		repositoryLoader.loadForHistoricProcessing(codeDirectory());
	}
	
	private RepositoryLoader createLoader() throws Exception {
		RepositoryType repositoryType = repository().getType();
		String loaderName = Identifier.fromConstant(repositoryType.name()).asClassName() + "Loader";
		return (RepositoryLoader) Class.forName("org.kalibro.core.loaders." + loaderName).newInstance();
	}
}
