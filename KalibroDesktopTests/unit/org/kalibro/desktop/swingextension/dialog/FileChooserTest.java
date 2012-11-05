package org.kalibro.desktop.swingextension.dialog;

import static javax.swing.JFileChooser.*;
import static org.junit.Assert.*;
import static org.kalibro.core.Environment.dotKalibro;

import java.awt.Component;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.tests.UnitTest;
import org.mockito.ArgumentCaptor;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(FileChooser.class)
public class FileChooserTest extends UnitTest {

	private Component parent;
	private JFileChooser nativeChooser;

	private FileChooser fileChooser;

	@Before
	public void setUp() throws Exception {
		parent = mock(Component.class);
		nativeChooser = mock(JFileChooser.class);
		whenNew(JFileChooser.class).withNoArguments().thenReturn(nativeChooser);
		fileChooser = new FileChooser(parent);
	}

	@Test
	public void shouldNotAllowMultipleSelection() {
		verify(nativeChooser).setMultiSelectionEnabled(false);
	}

	@Test
	public void shouldShowOnlyDirectoriesWhenChoosingDirectory() {
		fileChooser.chooseDirectoryToOpen();
		verify(nativeChooser).setFileSelectionMode(DIRECTORIES_ONLY);
		verify(nativeChooser).setAcceptAllFileFilterUsed(false);
	}

	@Test
	public void checkDirectoryFilter() {
		fileChooser.chooseDirectoryToOpen();
		FileFilter filter = captureFileFilter();

		assertEquals("Directories only", filter.getDescription());
		assertTrue(filter.accept(dotKalibro()));
		assertFalse(filter.accept(new File(dotKalibro(), "HelloWorld.c")));
	}

	private FileFilter captureFileFilter() {
		ArgumentCaptor<FileFilter> captor = ArgumentCaptor.forClass(FileFilter.class);
		verify(nativeChooser).setFileFilter(captor.capture());
		return captor.getValue();
	}

	@Test
	public void shouldShowOnlyFilesWhenChoosingFile() {
		fileChooser.chooseFileToSave("");
		verify(nativeChooser).setFileSelectionMode(FILES_ONLY);
	}

	@Test
	public void shouldReturnTrueWhenSelected() {
		when(nativeChooser.showOpenDialog(parent)).thenReturn(APPROVE_OPTION);
		when(nativeChooser.showSaveDialog(parent)).thenReturn(APPROVE_OPTION);

		assertTrue(fileChooser.chooseDirectoryToOpen());
		assertTrue(fileChooser.chooseFileToSave(""));
	}

	@Test
	public void shouldReturnFalseWhenCancelled() {
		when(nativeChooser.showOpenDialog(parent)).thenReturn(CANCEL_OPTION);
		when(nativeChooser.showSaveDialog(parent)).thenReturn(CANCEL_OPTION);

		assertFalse(fileChooser.chooseDirectoryToOpen());
		assertFalse(fileChooser.chooseFileToSave(""));
	}

	@Test
	public void shouldReturnFalseWhenClosed() {
		when(nativeChooser.showOpenDialog(parent)).thenReturn(ERROR_OPTION);
		when(nativeChooser.showSaveDialog(parent)).thenReturn(ERROR_OPTION);

		assertFalse(fileChooser.chooseDirectoryToOpen());
		assertFalse(fileChooser.chooseFileToSave(""));
	}

	@Test
	public void shouldSelectSuggestionInsideCurrentDirectory() {
		when(nativeChooser.getCurrentDirectory()).thenReturn(dotKalibro());

		String suggestion = "MyFile.txt";
		fileChooser.chooseFileToSave(suggestion);
		verify(nativeChooser).setSelectedFile(new File(dotKalibro(), suggestion));
	}

	@Test
	public void checkChoosenFile() {
		when(nativeChooser.getSelectedFile()).thenReturn(dotKalibro());
		assertEquals(dotKalibro(), fileChooser.getChosenFile());
	}
}