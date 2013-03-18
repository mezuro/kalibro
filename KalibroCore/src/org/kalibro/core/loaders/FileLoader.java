package org.kalibro.core.loaders;

import java.io.File;

/**
 * Abstract loader for simple files.
 * 
 * @author Carlos Morais
 * @author Diego Araújo
 */
abstract class FileLoader extends Loader {

	@Override
	public boolean isUpdatable(File directory) {
		return false;
	}
}
