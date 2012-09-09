package org.kalibro.desktop.swingextension.panel;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.TestCase;
import org.kalibro.core.model.enums.Language;
import org.kalibro.desktop.ComponentFinder;
import org.kalibro.desktop.swingextension.Button;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

public class ConfirmPanelTest extends TestCase {

	private ActionListener listener;

	private ConfirmPanel<Language> confirmPanel;
	private ComponentFinder finder;

	@Before
	public void setUp() {
		listener = PowerMockito.mock(ActionListener.class);
		confirmPanel = new ConfirmPanel<Language>(new LanguagePanelStub());
		finder = new ComponentFinder(confirmPanel);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldGet() {
		finder.find("language", LanguagePanelStub.class).set(Language.CPP);
		assertEquals(Language.CPP, confirmPanel.get());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSet() {
		confirmPanel.set(Language.JAVA);
		assertEquals(Language.JAVA, finder.find("language", LanguagePanelStub.class).get());
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