package org.kalibro.desktop.swingextension.dialog;

import static javax.swing.JFileChooser.*;

import java.awt.Component;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

public class FileChooser {

	private Component parent;
	private JFileChooser chooser;

	public FileChooser(Component parent) {
		this.parent = parent;
		chooser = new JFileChooser();
		chooser.setMultiSelectionEnabled(false);
	}

	public boolean chooseDirectoryToOpen() {
		chooser.setFileSelectionMode(DIRECTORIES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);
		chooser.setFileFilter(new DirectoryFileFilter());
		return chooser.showOpenDialog(parent) == APPROVE_OPTION;
	}

	public boolean chooseFileToSave(String fileNameSuggestion) {
		File suggested = new File(chooser.getCurrentDirectory(), fileNameSuggestion);
		chooser.setFileSelectionMode(FILES_ONLY);
		chooser.setSelectedFile(suggested);
		return chooser.showSaveDialog(parent) == APPROVE_OPTION;
	}

	public File getChosenFile() {
		return chooser.getSelectedFile();
	}

	private class DirectoryFileFilter extends FileFilter {

		@Override
		public String getDescription() {
			return "Directories only";
		}

		@Override
		public boolean accept(File file) {
			return file.isDirectory();
		}
	}
}