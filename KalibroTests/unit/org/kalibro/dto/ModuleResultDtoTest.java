package org.kalibro.dto;

import java.util.List;

import org.kalibro.MetricResult;
import org.kalibro.Module;
import org.kalibro.ModuleResult;
import org.kalibro.dao.MetricResultDao;
import org.kalibro.dao.ModuleResultDao;

public class ModuleResultDtoTest extends AbstractDtoTest<ModuleResult> {

	@Override
	protected ModuleResult loadFixture() {
		MetricResult metricResult = new MetricResultDtoTest().loadFixture();
		List<Module> ancestry = new ModuleDtoTest().loadFixture().inferAncestry();

		ModuleResult moduleResult = new ModuleResult(null, ancestry.get(0));
		moduleResult.addMetricResult(metricResult);
		moduleResult.addChild(new ModuleResult(moduleResult, ancestry.get(1)));
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