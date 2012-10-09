package org.kalibro.dto;

import org.kalibro.MetricResult;
import org.kalibro.Module;
import org.kalibro.ModuleResult;
import org.kalibro.dao.MetricResultDao;
import org.kalibro.dao.ModuleResultDao;

public class ModuleResultDtoTest extends AbstractDtoTest<ModuleResult> {

	@Override
	protected ModuleResult loadFixture() {
		Module module = mock(Module.class);
		ModuleResult parent = mock(ModuleResult.class);
		ModuleResult child = mock(ModuleResult.class);
		MetricResult metricResult = new MetricResultDtoTest().loadFixture();
		ModuleResult moduleResult = new ModuleResult(parent, module);
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