package org.kalibro.core.processing;

import java.io.File;
import java.io.FileFilter;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.kalibro.*;
import org.kalibro.core.Identifier;
import org.kalibro.core.loaders.RepositoryLoader;

/**
 * Loads source code from a {@link Repository} to a directory in the server file system.
 * 
 * @author Carlos Morais
 */
class LoadSourceTask extends ProcessSubtask<File> {

	private Repository repository;

	LoadSourceTask(Processing processing) {
		super(processing);
		repository = processing.getRepository();
	}

	@Override
	protected File compute() throws Exception {
		File codeDirectory = prepareCodeDirectory();
		createLoader().load(repository.getAddress(), codeDirectory);
		return codeDirectory;
	}

	private File prepareCodeDirectory() {
		Project project = repository.getProject();
		File loadDirectory = KalibroSettings.load().getServerSettings().getLoadDirectory();
		File projectDirectory = prepareDirectory(loadDirectory, project.getId(), project.getName());
		return prepareDirectory(projectDirectory, repository.getId(), repository.getName());
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
		if (directory.exists() && !directory.isDirectory())
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
		RepositoryType repositoryType = repository.getType();
		String loaderName = Identifier.fromConstant(repositoryType.name()).asClassName() + "Loader";
		return (RepositoryLoader) Class.forName("org.kalibro.core.loaders." + loaderName).newInstance();
	}

	@Override
	ProcessState getTaskState() {
		return ProcessState.LOADING;
	}

	@Override
	ProcessState getNextState() {
		return ProcessState.COLLECTING;
	}
}