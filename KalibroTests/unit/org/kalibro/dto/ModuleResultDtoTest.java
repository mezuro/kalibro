package org.kalibro.dto;

import org.kalibro.MetricResult;
import org.kalibro.ModuleResult;
import org.kalibro.dao.MetricResultDao;
import org.kalibro.dao.ModuleResultDao;

public class ModuleResultDtoTest extends AbstractDtoTest<ModuleResult> {

	@Override
	protected ModuleResult loadFixture() {
		ModuleResult child = mock(ModuleResult.class), parent = mock(ModuleResult.class);
		MetricResult metricResult = new MetricResultDtoTest().loadFixture();

		ModuleResult moduleResult = new ModuleResult(parent, new ModuleDtoTest().loadFixture());
		moduleResult.addMetricResult(metricResult);
		moduleResult.addChild(child);
		moduleResult.calculateGrade();
		return moduleResult;
	}

	@Override
	protected void registerLazyLoadExpectations() {
		whenLazy(ModuleResultDao.class, "parentOf", entity.getId()).thenReturn(entity.getParent());
		whenLazy(ModuleResultDao.class, "childrenOf", entity.getId()).thenReturn(entity.getChildren());
		whenLazy(MetricResultDao.class, "metricResultsOf", entity.getId()).thenReturn(entity.getMetricResults());
	}
}