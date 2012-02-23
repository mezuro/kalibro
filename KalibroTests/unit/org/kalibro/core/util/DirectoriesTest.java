package org.kalibro.core.util;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.BeforeClass;
import org.junit.Test;
import org.kalibro.KalibroTestCase;

public class DirectoriesTest extends KalibroTestCase {

	@BeforeClass
	public static void emmaCoverage() throws Exception {
		Directories.class.getDeclaredConstructor().newInstance();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkDirectories() {
		File home = new File(System.getProperty("user.home"));
		assertEquals(new File(home, ".kalibro"), Directories.kalibro());
		assertEquals(new File(home, ".kalibro/logs"), Directories.logs());
	}
}