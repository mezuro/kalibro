package org.kalibro.desktop.icon;

import org.junit.Test;
import org.kalibro.KalibroTestCase;

public class MethodIconTest extends KalibroTestCase {

	@Test(timeout = UNIT_TIMEOUT)
	public void checkPresenceOfIcon() {
		new MethodIcon();
	}
}