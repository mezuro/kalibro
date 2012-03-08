package org.kalibro.desktop.swingextension.panel;

import static org.mockito.Matchers.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;
import org.kalibro.desktop.ComponentFinder;
import org.kalibro.desktop.swingextension.Button;
import org.kalibro.desktop.swingextension.field.DoubleField;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

public class ConfirmPanelTest extends KalibroTestCase {

	private ActionListener listener;

	private ConfirmPanel confirmPanel;
	private ComponentFinder finder;

	@Before
	public void setUp() {
		listener = PowerMockito.mock(ActionListener.class);
		confirmPanel = new ConfirmPanel(new DoubleField("contentPane"));
		finder = new ComponentFinder(confirmPanel);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldShowContentPane() {
		finder.find("contentPane");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotifyCancelListener() {
		confirmPanel.addCancelListener(listener);
		clickAndVerify("cancel");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotifyOkListener() {
		confirmPanel.addOkListener(listener);
		clickAndVerify("ok");
	}

	private void clickAndVerify(String buttonName) {
		finder.find(buttonName, Button.class).doClick();
		Mockito.verify(listener).actionPerformed(any(ActionEvent.class));
	}
}