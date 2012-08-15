package org.kalibro.desktop.swingextension.dialog;

import static javax.swing.JFileChooser.*;
import static org.junit.Assert.*;

import java.awt.Component;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.Environment;
import org.kalibro.KalibroTestCase;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(FileChooser.class)
public class FileChooserTest extends KalibroTestCase {

	private Component parent;
	private JFileChooser nativeChooser;

	private FileChooser fileChooser;

	@Before
	public void setUp() throws Exception {
		parent = PowerMockito.mock(Component.class);
		nativeChooser = PowerMockito.mock(JFileChooser.class);
		PowerMockito.whenNew(JFileChooser.class).withNoArguments().thenReturn(nativeChooser);
		fileChooser = new FileChooser(parent);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotAllowMultipleSelection() {
		Mockito.verify(nativeChooser).setMultiSelectionEnabled(false);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldShowOnlyDirectoriesWhenChoosingDirectory() {
		fileChooser.chooseDirectoryToOpen();
		Mockito.verify(nativeChooser).setFileSelectionMode(DIRECTORIES_ONLY);
		Mockito.verify(nativeChooser).setAcceptAllFileFilterUsed(false);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkDirectoryFilter() {
		fileChooser.chooseDirectoryToOpen();
		FileFilter filter = captureFileFilter();

		assertEquals("Directories only", filter.getDescription());
		assertTrue(filter.accept(Environment.dotKalibro()));
		assertFalse(filter.accept(new File(Environment.dotKalibro(), "HelloWorld.c")));
	}

	private FileFilter captureFileFilter() {
		ArgumentCaptor<FileFilter> captor = ArgumentCaptor.forClass(FileFilter.class);
		Mockito.verify(nativeChooser).setFileFilter(captor.capture());
		return captor.getValue();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldShowOnlyFilesWhenChoosingFile() {
		fileChooser.chooseFileToSave("");
		Mockito.verify(nativeChooser).setFileSelectionMode(FILES_ONLY);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldReturnTrueWhenSelected() {
		PowerMockito.when(nativeChooser.showOpenDialog(parent)).thenReturn(APPROVE_OPTION);
		assertTrue(fileChooser.chooseDirectoryToOpen());

		PowerMockito.when(nativeChooser.showSaveDialog(parent)).thenReturn(APPROVE_OPTION);
		assertTrue(fileChooser.chooseFileToSave(""));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldReturnFalseWhenCancelled() {
		PowerMockito.when(nativeChooser.showOpenDialog(parent)).thenReturn(CANCEL_OPTION);
		assertFalse(fileChooser.chooseDirectoryToOpen());

		PowerMockito.when(nativeChooser.showSaveDialog(parent)).thenReturn(CANCEL_OPTION);
		assertFalse(fileChooser.chooseFileToSave(""));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldReturnFalseWhenClosed() {
		PowerMockito.when(nativeChooser.showOpenDialog(parent)).thenReturn(ERROR_OPTION);
		assertFalse(fileChooser.chooseDirectoryToOpen());

		PowerMockito.when(nativeChooser.showSaveDialog(parent)).thenReturn(ERROR_OPTION);
		assertFalse(fileChooser.chooseFileToSave(""));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSelectSuggestionInsideCurrentDirectory() {
		PowerMockito.when(nativeChooser.getCurrentDirectory()).thenReturn(Environment.dotKalibro());

		String suggestion = "MyFile.txt";
		fileChooser.chooseFileToSave(suggestion);
		Mockito.verify(nativeChooser).setSelectedFile(new File(Environment.dotKalibro(), suggestion));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkChoosenFile() {
		PowerMockito.when(nativeChooser.getSelectedFile()).thenReturn(Environment.dotKalibro());
		assertEquals(Environment.dotKalibro(), fileChooser.getChosenFile());
	}
}