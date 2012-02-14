package org.kalibro.core.loaders;

import java.util.List;

import org.kalibro.core.model.Repository;

public interface ProjectLoader {

	public List<String> getValidationCommands();

	public boolean supportsAuthentication();

	public List<String> getLoadCommands(Repository repository, String loadPath);
}