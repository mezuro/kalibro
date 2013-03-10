package org.kalibro.dto;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;
import org.kalibro.Module;
import org.kalibro.ModuleResult;
import org.kalibro.dao.MetricResultDao;
import org.kalibro.dao.ModuleResultDao;
import org.powermock.reflect.Whitebox;

public class ModuleResultDtoTest extends AbstractDtoTest<ModuleResult> {

	@Override
	protected ModuleResult loadFixture() {
		Module module = new ModuleDtoTest().loadFixture();
		ModuleResult parent = new ModuleResult(null, module.inferParent().inferParent());
		ModuleResult moduleResult = new ModuleResult(parent, module.inferParent());
		moduleResult.addMetricResult(new MetricResultDtoTest().loadFixture());
		moduleResult.setChildren(sortedSet(new ModuleResult(moduleResult, module)));
		Whitebox.setInternalState(moduleResult, "height", 1);
		return moduleResult;
	}

	@Override
	protected void registerLazyLoadExpectations() throws Exception {
		Long parentId = new Random().nextLong();
		doReturn(parentId).when(dto, "parentId");
		whenLazy(ModuleResultDao.class, "get", parentId).thenReturn(entity.getParent());
		whenLazy(ModuleResultDao.class, "childrenOf", entity.getId()).thenReturn(entity.getChildren());
		whenLazy(MetricResultDao.class, "metricResultsOf", entity.getId()).thenReturn(entity.getMetricResults());
	}

	@Test
	public void parentShouldBeNullForNullParentId() throws Exception {
		when(dto, "parentId").thenReturn(null);
		assertFalse(dto.convert().hasParent());
	}
}