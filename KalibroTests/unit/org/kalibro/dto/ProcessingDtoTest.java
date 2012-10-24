package org.kalibro.dto;

import org.kalibro.ProcessState;
import org.kalibro.Processing;
import org.kalibro.Repository;
import org.kalibro.dao.ModuleResultDao;
import org.kalibro.dao.RepositoryDao;

public class ProcessingDtoTest extends AbstractDtoTest<Processing> {

	@Override
	protected Processing loadFixture() {
		Processing processing = new Processing(mock(Repository.class));
		processing.setStateTime(ProcessState.LOADING, 2000);
		processing.setState(ProcessState.COLLECTING);
		processing.setError(new Throwable());
		return processing;
	}

	@Override
	protected void registerLazyLoadExpectations() {
		whenLazy(RepositoryDao.class, "repositoryOf", entity.getId()).thenReturn(entity.getRepository());
		whenLazy(ModuleResultDao.class, "resultsRootOf", entity.getId()).thenReturn(entity.getResultsRoot());
	}
}