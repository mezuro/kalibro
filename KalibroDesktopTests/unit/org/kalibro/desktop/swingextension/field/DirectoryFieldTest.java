package org.kalibro.desktop.swingextension.field;

import static org.junit.Assert.*;
import static org.kalibro.core.Environment.dotKalibro;

import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.desktop.swingextension.Button;
import org.kalibro.desktop.swingextension.dialog.ErrorDialog;
import org.kalibro.desktop.swingextension.dialog.FileChooser;
import org.kalibro.desktop.tests.ComponentFinder;
import org.kalibro.tests.UnitTest;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.*")
@PrepareOnlyThisForTest(DirectoryField.class)
public class DirectoryFieldTest extends UnitTest {

	private static final String NAME = "DirectoryFieldTest name";

	private FileChooser chooser;
	private ErrorDialog errorDialog;

	private DirectoryField field;
	private ComponentFinder finder;

	@Before
	public void setUp() throws Exception {
		mockFileChooser();
		mockErrorDialog();
		field = new DirectoryField(NAME);
		finder = new ComponentFinder(field);
	}

	private void mockFileChooser() throws Exception {
		chooser = mock(FileChooser.class);
		whenNew(FileChooser.class).withArguments(any(DirectoryField.class)).thenReturn(chooser);
	}

	private void mockErrorDialog() throws Exception {
		errorDialog = mock(ErrorDialog.class);
		whenNew(ErrorDialog.class).withArguments(any(DirectoryField.class)).thenReturn(errorDialog);
	}

	@Test
	public void shouldSetName() {
		assertEquals(NAME, field.getName());
	}

	@Test
	public void valueShouldBeNullByDefault() {
		assertNull(field.get());
	}

	@Test
	public void shouldSetAndGet() {
		field.set(dotKalibro());
		assertEquals(dotKalibro(), field.get());
	}

	@Test
	public void shouldNotAcceptInexistentDirectory() {
		File file = mock(File.class);
		when(file.exists()).thenReturn(false);
		shouldNotAccept(file);
	}

	@Test
	public void shouldNotAcceptFile() {
		File file = mock(File.class);
		when(file.isDirectory()).thenReturn(false);
		shouldNotAccept(file);
	}

	private void shouldNotAccept(File file) {
		field.set(file);
		verify(errorDialog).show("\"" + file + "\" is not a valid directory");
	}

	@Test
	public void shouldUpdateDirectoryWhenPathChanges() {
		pathField().setText(dotKalibro().getAbsolutePath());
		simulateFocusLost(false, null);
		assertEquals(dotKalibro(), field.get());
	}

	@Test
	public void shouldNotUpdateDirectoryIfPathFieldLostFocusForBrowseButton() {
		pathField().setText(dotKalibro().getAbsolutePath());
		simulateFocusLost(false, browseButton());
		assertNull(field.get());
	}

	@Test
	public void shouldNotUpdateDirectoryIfFocusEventIsTemporary() {
		pathField().setText(dotKalibro().getAbsolutePath());
		simulateFocusLost(true, null);
		assertNull(field.get());
	}

	private void simulateFocusLost(boolean temporary, Component opposite) {
		FocusEvent event = new FocusEvent(pathField(), FocusEvent.FOCUS_LOST, temporary, opposite);
		for (FocusListener listener : pathField().getFocusListeners())
			listener.focusLost(event);
	}

	@Test
	public void shouldChooseFileWithFileChooserWhenBrowseButtonClicked() {
		when(chooser.chooseDirectoryToOpen()).thenReturn(true);
		when(chooser.getChosenFile()).thenReturn(dotKalibro());
		browseButton().doClick();
		assertEquals(dotKalibro(), field.get());
		assertEquals(dotKalibro().getAbsolutePath(), pathField().get());
	}

	@Test
	public void shouldNotSetDirectoryIfNoDirectoryIsSelectedInFileChooser() {
		when(chooser.chooseDirectoryToOpen()).thenReturn(false);
		when(chooser.getChosenFile()).thenReturn(dotKalibro());
		browseButton().doClick();
		assertNull(field.get());
		assertEquals("", pathField().get());
	}

	private StringField pathField() {
		return finder.find("path", StringField.class);
	}

	private Button browseButton() {
		return finder.find("browse", Button.class);
	}
}