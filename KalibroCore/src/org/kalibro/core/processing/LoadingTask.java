package org.kalibro.core.processing;

import java.io.File;
import java.io.FileFilter;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.kalibro.KalibroException;
import org.kalibro.Repository;
import org.kalibro.RepositoryType;
import org.kalibro.core.Identifier;
import org.kalibro.core.loaders.Loader;

/**
 * Loads source code from a {@link Repository} to a directory in the server file system.
 * 
 * @author Carlos Morais
 */
class LoadingTask extends ProcessSubtask {

	LoadingTask(ProcessContext context) {
		super(context);
	}

	@Override
	protected void perform() throws Exception {
		File codeDirectory = context.codeDirectory();
		prepare(codeDirectory.getParentFile());
		prepare(codeDirectory);
		createLoader().load(context.repository().getAddress(), codeDirectory);
	}

	private File prepare(File directory) {
		assertParentExists(directory);
		File[] withSameSuffix = filesWithSameSuffixOf(directory);
		assertSuffixExclusivity(withSameSuffix);
		if (withSameSuffix.length == 1)
			withSameSuffix[0].renameTo(directory);
		assertIsDirectory(directory);
		return directory;
	}

	private void assertParentExists(File directory) {
		if (!directory.getParentFile().mkdirs())
			throw new KalibroException("Could not create directory: " + directory);
	}

	private File[] filesWithSameSuffixOf(File directory) {
		String directoryName = directory.getName();
		String suffix = directoryName.substring(directoryName.lastIndexOf('-'));
		return directory.getParentFile().listFiles((FileFilter) new SuffixFileFilter(suffix));
	}

	private void assertSuffixExclusivity(File[] withSameSuffix) {
		if (withSameSuffix.length > 1)
			for (File file : withSameSuffix)
				FileUtils.deleteQuietly(file);
	}

	private void assertIsDirectory(File directory) {
		if (directory.exists() && !directory.isDirectory())
			directory.delete();
	}

	private Loader createLoader() throws Exception {
		RepositoryType repositoryType = context.repository().getType();
		String loaderName = Identifier.fromConstant(repositoryType.name()).asClassName() + "Loader";
		return (Loader) Class.forName("org.kalibro.core.loaders." + loaderName).newInstance();
	}
}