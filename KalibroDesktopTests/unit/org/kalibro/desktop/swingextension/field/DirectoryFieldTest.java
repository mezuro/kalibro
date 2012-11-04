package org.kalibro.desktop.swingextension.field;

import static org.junit.Assert.*;

import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.core.Environment;
import org.kalibro.desktop.swingextension.Button;
import org.kalibro.desktop.swingextension.dialog.ErrorDialog;
import org.kalibro.desktop.swingextension.dialog.FileChooser;
import org.kalibro.desktop.tests.ComponentFinder;
import org.kalibro.tests.UnitTest;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.*")
@PrepareOnlyThisForTest(DirectoryField.class)
public class DirectoryFieldTest extends UnitTest {

	private FileChooser chooser;
	private ErrorDialog errorDialog;

	private DirectoryField field;
	private ComponentFinder finder;

	@Before
	public void setUp() throws Exception {
		mockFileChooser();
		mockErrorDialog();
		field = new DirectoryField("");
		finder = new ComponentFinder(field);
	}

	private void mockFileChooser() throws Exception {
		chooser = PowerMockito.mock(FileChooser.class);
		PowerMockito.whenNew(FileChooser.class).withArguments(any(DirectoryField.class)).thenReturn(chooser);
	}

	private void mockErrorDialog() throws Exception {
		errorDialog = PowerMockito.mock(ErrorDialog.class);
		PowerMockito.whenNew(ErrorDialog.class).withArguments(any(DirectoryField.class)).thenReturn(errorDialog);
	}

	@Test
	public void valueShouldBeNullByDefault() {
		assertNull(field.get());
	}

	@Test
	public void shouldSetAndGet() {
		field.set(Environment.dotKalibro());
		assertEquals(Environment.dotKalibro(), field.get());
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
		Mockito.verify(errorDialog).show("\"" + file + "\" is not a valid directory");
	}

	@Test
	public void shouldUpdateDirectoryWhenPathChanges() {
		pathField().setText(Environment.dotKalibro().getAbsolutePath());
		simulateFocusLost(false, null);
		assertEquals(Environment.dotKalibro(), field.get());
	}

	@Test
	public void shouldNotUpdateDirectoryIfPathFieldLostFocusForBrowseButton() {
		pathField().setText(Environment.dotKalibro().getAbsolutePath());
		simulateFocusLost(false, browseButton());
		assertNull(field.get());
	}

	@Test
	public void shouldNotUpdateDirectoryIfFocusEventIsTemporary() {
		pathField().setText(Environment.dotKalibro().getAbsolutePath());
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
		PowerMockito.when(chooser.chooseDirectoryToOpen()).thenReturn(true);
		PowerMockito.when(chooser.getChosenFile()).thenReturn(Environment.dotKalibro());
		browseButton().doClick();
		assertEquals(Environment.dotKalibro(), field.get());
		assertEquals(Environment.dotKalibro().getAbsolutePath(), pathField().get());
	}

	@Test
	public void shouldNotSetDirectoryIfNoDirectoryIsSelectedInFileChooser() {
		PowerMockito.when(chooser.chooseDirectoryToOpen()).thenReturn(false);
		PowerMockito.when(chooser.getChosenFile()).thenReturn(Environment.dotKalibro());
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