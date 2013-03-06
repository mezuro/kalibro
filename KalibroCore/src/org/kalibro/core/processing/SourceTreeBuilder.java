package org.kalibro.core.processing;

import static org.kalibro.Granularity.*;

import org.kalibro.Module;
import org.kalibro.ModuleResult;
import org.kalibro.core.persistence.ModuleResultDatabaseDao;

/**
 * Saves module results for native modules, appending them to the existing source tree.
 * 
 * @author Carlos Morais
 */
class SourceTreeBuilder {

	private Long processingId;
	private String repositoryName;
	private ModuleResultDatabaseDao dao;

	SourceTreeBuilder(Long processingId, String repositoryName, ModuleResultDatabaseDao dao) {
		this.processingId = processingId;
		this.repositoryName = repositoryName;
		this.dao = dao;
	}

	Long save(Module nativeModule) {
		return getResultFor(nativeModule).getId();
	}

	private ModuleResult getResultFor(Module module) {
		if (module == null)
			return null;
		ModuleResult moduleResult = dao.getResultFor(module, processingId);
		if (moduleResult == null) {
			ModuleResult parent = getResultFor(module.inferParent());
			moduleResult = dao.save(newResult(parent, module), processingId);
		} else if (moduleResult.getModule().getGranularity() != module.getGranularity()) {
			moduleResult.getModule().setGranularity(module.getGranularity());
			moduleResult = dao.save(moduleResult, processingId);
		}
		return moduleResult;
	}

	private ModuleResult newResult(ModuleResult parent, Module module) {
		if (module.getGranularity() == SOFTWARE)
			return new ModuleResult(null, new Module(SOFTWARE, repositoryName));
		return new ModuleResult(parent, module);
	}
}