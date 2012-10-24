package org.kalibro.core.loaders;

import java.util.Arrays;
import java.util.List;

/**
 * Loader for CVS repositories.
 * 
 * @author Carlos Morais
 */
public class CvsLoader extends RepositoryLoader {

	@Override
	public List<String> validationCommands() {
		return Arrays.asList("cvs --version");
	}

	@Override
	public List<String> loadCommands(String address, boolean update) {
		if (update)
			return Arrays.asList("cvs update");
		return Arrays.asList("cvs -z3 -d " + address + " checkout -d . -P .");
	}
}