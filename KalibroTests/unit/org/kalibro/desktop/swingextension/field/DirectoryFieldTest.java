package org.kalibro.desktop.swingextension.field;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;

import java.awt.event.FocusEvent;
import java.io.File;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.KalibroTestCase;
import org.kalibro.desktop.ComponentFinder;
import org.kalibro.desktop.swingextension.Button;
import org.kalibro.desktop.swingextension.dialog.ErrorDialog;
import org.kalibro.desktop.swingextension.dialog.FileChooser;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.*")
@PrepareOnlyThisForTest(DirectoryField.class)
public class DirectoryFieldTest extends KalibroTestCase {

	@BeforeClass
	public static void emmaCoverage() {
		new DirectoryField("").focusGained(null);
	}

	private FileChooser chooser;
	private ErrorDialog errorDialog;

	private DirectoryField field;
	private ComponentFinder finder;

	@Before
	public void setUp() throws Exception {
		createMocks();
		field = new DirectoryField("");
		finder = new ComponentFinder(field);
	}

	private void createMocks() throws Exception {
		chooser = PowerMockito.mock(FileChooser.class);
		errorDialog = PowerMockito.mock(ErrorDialog.class);
		PowerMockito.whenNew(FileChooser.class).withArguments(any(DirectoryField.class)).thenReturn(chooser);
		PowerMockito.whenNew(ErrorDialog.class).withArguments(any(DirectoryField.class)).thenReturn(errorDialog);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void directoryShouldBeNullByDefault() {
		assertNull(field.getDirectory());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testShowRetrieve() {
		field.show(TESTS_DIRECTORY);
		assertSame(TESTS_DIRECTORY, field.get());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotAcceptInexistentDirectory() {
		shouldNotAccept(HELLO_WORLD_DIRECTORY);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotAcceptFile() {
		File file = new File(TESTS_DIRECTORY, "HelloWorld.c");
		shouldNotAccept(file);
	}

	private void shouldNotAccept(File file) {
		field.setDirectory(file);
		Mockito.verify(errorDialog).show("\"" + file.getAbsolutePath() + "\" is not a valid directory");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldUpdateDirectoryWhenPathChanges() {
		pathField().setText(TESTS_DIRECTORY.getAbsolutePath());
		field.focusLost(new FocusEvent(field, 0));
		assertEquals(TESTS_DIRECTORY, field.getDirectory());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotUpdateDirectoryIfFocusEventIsTemporary() {
		pathField().setText(TESTS_DIRECTORY.getAbsolutePath());
		field.focusLost(new FocusEvent(field, 0, true));
		assertNull(field.getDirectory());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldChooseFileWithFileChooserWhenBrowseButtonClicked() {
		PowerMockito.when(chooser.chooseDirectoryToOpen()).thenReturn(true);
		PowerMockito.when(chooser.getChosenFile()).thenReturn(TESTS_DIRECTORY);
		browseButton().doClick();
		assertEquals(TESTS_DIRECTORY, field.getDirectory());
		assertEquals(TESTS_DIRECTORY.getAbsolutePath(), pathField().get());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotSetDirectoryIfNoDirectoryIsSelectedInFileChooser() {
		PowerMockito.when(chooser.chooseDirectoryToOpen()).thenReturn(false);
		PowerMockito.when(chooser.getChosenFile()).thenReturn(TESTS_DIRECTORY);
		browseButton().doClick();
		assertNull(field.getDirectory());
		assertEquals("", pathField().get());
	}

	private StringField pathField() {
		return finder.find("path", StringField.class);
	}

	private Button browseButton() {
		return finder.find("browse", Button.class);
	}
}