package org.kalibro.dto;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;
import org.kalibro.ProcessState;
import org.kalibro.Processing;
import org.kalibro.dao.ModuleResultDao;

public class ProcessingDtoTest extends AbstractDtoTest<Processing> {

	@Override
	protected Processing loadFixture() {
		Processing processing = new Processing();
		processing.setStateTime(ProcessState.LOADING, 2000);
		processing.setState(ProcessState.COLLECTING);
		processing.setError(new Throwable());
		return processing;
	}

	@Override
	protected void registerLazyLoadExpectations() throws Exception {
		Long resultsRootId = new Random().nextLong();
		doReturn(resultsRootId).when(dto, "resultsRootId");
		whenLazy(ModuleResultDao.class, "get", resultsRootId).thenReturn(entity.getResultsRoot());
	}

	@Test
	public void resultsRootShouldBeNullForNullResultsRootId() throws Exception {
		when(dto, "resultsRootId").thenReturn(null);
		assertNull(dto.convert().getResultsRoot());
	}
}