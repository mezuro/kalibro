package org.kalibro.core.loaders;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FalseFileFilter;
import org.apache.commons.io.filefilter.NameFileFilter;

/**
 * Abstract loader for version control systems.
 * 
 * @author Carlos Morais
 * @author Diego Araújo
 */
abstract class RepositoryLoader extends Loader {

	@Override
	protected boolean isUpdatable(File directory) {
		NameFileFilter nameFilter = new NameFileFilter(metadataDirectoryName());
		return FileUtils.iterateFiles(directory, FalseFileFilter.INSTANCE, nameFilter).hasNext();
	}

	protected abstract String metadataDirectoryName();
}