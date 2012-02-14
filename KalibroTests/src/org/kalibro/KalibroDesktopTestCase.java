package org.kalibro;

import static org.mockito.Matchers.*;

import java.io.File;

import javax.swing.WindowConstants;

import org.apache.commons.io.FileUtils;
import org.fest.swing.core.BasicRobot;
import org.fest.swing.core.Robot;
import org.fest.swing.fixture.DialogFixture;
import org.fest.swing.fixture.FrameFixture;
import org.fest.swing.fixture.WindowFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.kalibro.core.KalibroLocal;
import org.kalibro.core.persistence.database.DatabaseDaoFactory;
import org.kalibro.core.persistence.database.derby.DerbyTestSettings;
import org.kalibro.core.settings.DatabaseSettings;
import org.kalibro.core.util.Directories;
import org.kalibro.desktop.KalibroController;
import org.kalibro.desktop.KalibroFrame;
import org.kalibro.desktop.settings.SettingsDialog;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore({"org.xml.*", "javax.*"})
@PrepareForTest({Directories.class, KalibroController.class, KalibroLocal.class})
public class KalibroDesktopTestCase extends KalibroTestCase {

	protected static final File KALIBRO_DIRECTORY = new File(TESTS_DIRECTORY, "kalibro");
	protected static final File LOGS_DIRECTORY = new File(KALIBRO_DIRECTORY, "logs");

	private KalibroFrame kalibroFrame;
	private SettingsDialog settingsDialog;

	protected WindowFixture<?> fixture;

	@Before
	public void prepareEnvironment() throws Exception {
		changeDirectories();
		changeDaoFactory();
		prepareWindows();
	}

	private void changeDirectories() {
		LOGS_DIRECTORY.mkdirs();
		PowerMockito.mockStatic(Directories.class);
		PowerMockito.when(Directories.logs()).thenReturn(LOGS_DIRECTORY);
		PowerMockito.when(Directories.kalibro()).thenReturn(KALIBRO_DIRECTORY);
	}

	private void changeDaoFactory() throws Exception {
		DatabaseDaoFactory factory = new DatabaseDaoFactory(new DerbyTestSettings());
		PowerMockito.whenNew(DatabaseDaoFactory.class).withArguments(any(DatabaseSettings.class)).thenReturn(factory);

	}

	private void prepareWindows() throws Exception {
		KalibroController controller = new KalibroController();
		settingsDialog = new SettingsDialog();
		kalibroFrame = new KalibroFrame(controller);
		kalibroFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		PowerMockito.whenNew(KalibroController.class).withNoArguments().thenReturn(controller);
		PowerMockito.whenNew(KalibroFrame.class).withArguments(controller).thenReturn(kalibroFrame);
		PowerMockito.whenNew(SettingsDialog.class).withNoArguments().thenReturn(settingsDialog);
	}

	@After
	public void deleteKalibroDirectory() {
		fixture.cleanUp();
		FileUtils.deleteQuietly(KALIBRO_DIRECTORY);
	}

	protected void startKalibroDesktop() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				KalibroController.main(null);
			}
		}).start();
	}

	protected void captureKalibroFrame() {
		fixture = new FrameFixture(robot(), kalibroFrame);
	}

	protected void captureSettingsDialog() {
		fixture = new DialogFixture(robot(), settingsDialog);
	}

	private Robot robot() {
		return (fixture == null) ? BasicRobot.robotWithCurrentAwtHierarchy() : fixture.robot;
	}
}