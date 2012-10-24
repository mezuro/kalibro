package org.kalibro.dto;

import org.kalibro.Module;
import org.kalibro.ModuleResult;
import org.kalibro.dao.MetricResultDao;
import org.kalibro.dao.ModuleResultDao;

public class ModuleResultDtoTest extends AbstractDtoTest<ModuleResult> {

	@Override
	protected ModuleResult loadFixture() {
		Module module = new ModuleDtoTest().loadFixture();
		ModuleResult parent = new ModuleResult(null, module.inferParent().inferParent());
		ModuleResult moduleResult = new ModuleResult(parent, module.inferParent());
		moduleResult.addMetricResult(new MetricResultDtoTest().loadFixture());
		moduleResult.addChild(new ModuleResult(moduleResult, module));
		return moduleResult;
	}

	@Override
	protected void registerLazyLoadExpectations() {
		whenLazy(ModuleResultDao.class, "parentOf", entity.getId()).thenReturn(entity.getParent());
		whenLazy(ModuleResultDao.class, "childrenOf", entity.getId()).thenReturn(entity.getChildren());
		whenLazy(MetricResultDao.class, "metricResultsOf", entity.getId()).thenReturn(entity.getMetricResults());
	}
}