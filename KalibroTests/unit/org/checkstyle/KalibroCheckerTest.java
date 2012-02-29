package org.checkstyle;

import com.puppycrawl.tools.checkstyle.Checker;
import com.puppycrawl.tools.checkstyle.api.AuditListener;
import com.puppycrawl.tools.checkstyle.api.Configuration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.KalibroTestCase;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Checker.class, FileUtils.class, KalibroChecker.class})
public class KalibroCheckerTest extends KalibroTestCase {

	private Checker checker;
	private AuditListener listener;
	private Configuration configuration;

	private KalibroChecker kalibroChecker;

	@Before
	public void setUp() throws Exception {
		mockChecker();
		listener = PowerMockito.mock(AuditListener.class);
		configuration = PowerMockito.mock(Configuration.class);
		kalibroChecker = new KalibroChecker(listener, configuration);
	}

	private void mockChecker() throws Exception {
		checker = PowerMockito.mock(Checker.class);
		PowerMockito.whenNew(Checker.class).withNoArguments().thenReturn(checker);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSetClassLoader() {
		Mockito.verify(checker).setModuleClassLoader(Checker.class.getClassLoader());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldAddListenerToChecker() {
		Mockito.verify(checker).addListener(listener);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldConfigureChecker() throws Exception {
		Mockito.verify(checker).configure(configuration);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldProcessFilesAndDestroyChecker() {
		List<File> files = mockListedFiles();
		kalibroChecker.process(null);

		InOrder order = Mockito.inOrder(checker, checker);
		order.verify(checker).process(files);
		order.verify(checker).destroy();
	}

	private List<File> mockListedFiles() {
		List<File> files = new ArrayList<File>();
		PowerMockito.mockStatic(FileUtils.class);
		PowerMockito.when(FileUtils.listFiles(null, new String[]{"java"}, true)).thenReturn(files);
		return files;
	}
}