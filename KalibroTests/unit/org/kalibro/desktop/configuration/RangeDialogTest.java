package org.kalibro.desktop.configuration;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.model.Range;
import org.kalibro.core.model.RangeFixtures;
import org.kalibro.core.model.RangeLabel;
import org.kalibro.desktop.ComponentFinder;
import org.kalibro.desktop.swingextension.Button;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

public class RangeDialogTest extends KalibroTestCase {

	private Range range;

	private RangeDialog dialog;
	private ComponentFinder finder;

	@Before
	public void setUp() {
		range = RangeFixtures.amlocRange(RangeLabel.BAD);
		dialog = new RangeDialog();
		finder = new ComponentFinder(dialog);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldShow() {
		dialog.setRange(range);
		assertDeepEquals(range, rangePanel().get());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRetrieve() {
		rangePanel().set(range);
		assertDeepEquals(range, dialog.getRange());
	}

	private RangePanel rangePanel() {
		return finder.find("range", RangePanel.class);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldAddListenerToOkButton() {
		ActionListener listener = PowerMockito.mock(ActionListener.class);
		dialog.addOkListener(listener);

		button("ok").doClick();
		Mockito.verify(listener).actionPerformed(any(ActionEvent.class));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldCloseDialogOnCancel() {
		assertTrue(dialog.isDisplayable());
		button("cancel").doClick();
		assertFalse(dialog.isDisplayable());
	}

	private Button button(String name) {
		return finder.find(name, Button.class);
	}
}