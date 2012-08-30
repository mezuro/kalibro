package org.kalibro.desktop.swingextension.menu;

import static org.mockito.Matchers.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.TestCase;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

public class MenuItemTest extends TestCase {

	private ActionListener listener;

	private MenuItem item;

	@Before
	public void setUp() {
		listener = PowerMockito.mock(ActionListener.class);
		item = new MenuItem("", "My menu", 'M', listener);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotifyListener() {
		item.doClick();
		Mockito.verify(listener).actionPerformed(any(ActionEvent.class));
	}
}