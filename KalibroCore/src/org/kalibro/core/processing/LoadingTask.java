package org.kalibro.core.processing;

import java.io.File;
import java.io.FileFilter;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.kalibro.KalibroSettings;
import org.kalibro.Repository;
import org.kalibro.RepositoryType;
import org.kalibro.core.Identifier;
import org.kalibro.core.loaders.RepositoryLoader;

/**
 * Loads source code from a {@link Repository} to a directory in the server file system.
 * 
 * @author Carlos Morais
 */
class LoadingTask extends ProcessSubtask {

	@Override
	protected void perform() throws Exception {
		RepositoryLoader loader = createLoader();
		prepareCodeDirectory(loader);
		loader.load(repository().getAddress(), codeDirectory());
	}

	private void prepareCodeDirectory(RepositoryLoader loader) {
		File loadDirectory = KalibroSettings.load().getServerSettings().getLoadDirectory();
		File projectDirectory = prepareDirectory(loadDirectory, project().getId(), project().getName());
		File repositoryDirectory = prepareDirectory(projectDirectory, repository().getId(), repository().getName());
		if (repositoryDirectory.exists() && ! loader.isUpdatable(repositoryDirectory))
			repositoryDirectory.delete();
		setCodeDirectory(repositoryDirectory);
	}

	private File prepareDirectory(File parent, Long id, String name) {
		parent.mkdirs();
		String suffix = "-" + id;
		File directory = new File(parent, Identifier.fromText(name).asClassName() + suffix);
		File[] withSuffix = listWithSuffix(parent, suffix);
		if (withSuffix.length > 1)
			deleteAll(withSuffix);
		else if (withSuffix.length == 1)
			withSuffix[0].renameTo(directory);
		if (directory.exists() && ! directory.isDirectory())
			directory.delete();
		return directory;
	}

	private File[] listWithSuffix(File directory, String suffix) {
		return directory.listFiles((FileFilter) new SuffixFileFilter(suffix));
	}

	private void deleteAll(File[] files) {
		for (File file : files)
			FileUtils.deleteQuietly(file);
	}

	private RepositoryLoader createLoader() throws Exception {
		RepositoryType repositoryType = repository().getType();
		String loaderName = Identifier.fromConstant(repositoryType.name()).asClassName() + "Loader";
		return (RepositoryLoader) Class.forName("org.kalibro.core.loaders." + loaderName).newInstance();
	}
}