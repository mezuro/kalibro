package org.kalibro.dto;

import java.util.SortedSet;

import org.kalibro.MetricResult;
import org.kalibro.Module;
import org.kalibro.ModuleResult;
import org.kalibro.dao.MetricResultDao;
import org.kalibro.dao.ModuleResultDao;

/**
 * Data transfer object for {@link ModuleResult}.
 * 
 * @author Carlos Morais
 */
public abstract class ModuleResultDto extends DataTransferObject<ModuleResult> {

	@Override
	public ModuleResult convert() {
		ModuleResult parent = parentId() == null ? null : parent();
		ModuleResult moduleResult = new ModuleResult(parent, module());
		setId(moduleResult, id());
		set(moduleResult, "height", height());
		moduleResult.setGrade(grade());
		moduleResult.setChildren(children());
		moduleResult.setMetricResults(metricResults());
		return moduleResult;
	}

	public abstract Long id();

	public abstract Module module();

	public abstract Double grade();

	private ModuleResult parent() {
		return DaoLazyLoader.createProxy(ModuleResultDao.class, "get", parentId());
	}

	public abstract Long parentId();

	private SortedSet<ModuleResult> children() {
		return DaoLazyLoader.createProxy(ModuleResultDao.class, "childrenOf", id());
	}

	private SortedSet<MetricResult> metricResults() {
		return DaoLazyLoader.createProxy(MetricResultDao.class, "metricResultsOf", id());
	}

	public abstract Integer height();
}