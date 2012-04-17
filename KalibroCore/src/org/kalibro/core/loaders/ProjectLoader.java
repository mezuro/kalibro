package org.kalibro.core.loaders;

import java.util.List;

import org.kalibro.core.model.Repository;

public interface ProjectLoader {

	List<String> getValidationCommands();

	boolean supportsAuthentication();

	List<String> getLoadCommands(Repository repository, String loadPath);
	
	List<String> getUpdateCommands(Repository repository, String loadPath);
}