package org.kalibro.desktop.swingextension.panel;

import static org.junit.Assert.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.Language;
import org.kalibro.desktop.swingextension.Button;
import org.kalibro.desktop.tests.ComponentFinder;
import org.kalibro.tests.UnitTest;
import org.mockito.ArgumentCaptor;

public class ConfirmPanelTest extends UnitTest {

	private ActionListener listener;

	private ConfirmPanel<Language> confirmPanel;
	private ComponentFinder finder;

	@Before
	public void setUp() {
		listener = mock(ActionListener.class);
		confirmPanel = new ConfirmPanel<Language>(new LanguagePanel());
		finder = new ComponentFinder(confirmPanel);
	}

	@Test
	public void shouldGet() {
		finder.find("language", LanguagePanel.class).set(Language.CPP);
		assertEquals(Language.CPP, confirmPanel.get());
	}

	@Test
	public void shouldSet() {
		confirmPanel.set(Language.JAVA);
		assertEquals(Language.JAVA, finder.find("language", LanguagePanel.class).get());
	}

	@Test
	public void shouldNotifyCancelListener() {
		confirmPanel.addCancelListener(listener);
		clickAndVerify("cancel");
	}

	@Test
	public void shouldNotifyOkListener() {
		confirmPanel.addOkListener(listener);
		clickAndVerify("ok");
	}

	private void clickAndVerify(String buttonName) {
		Button button = finder.find(buttonName, Button.class);
		button.doClick();

		ArgumentCaptor<ActionEvent> captor = ArgumentCaptor.forClass(ActionEvent.class);
		verify(listener).actionPerformed(captor.capture());
		assertSame(button, captor.getValue().getSource());
	}
}