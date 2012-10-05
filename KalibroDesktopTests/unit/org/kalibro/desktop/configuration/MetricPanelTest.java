package org.kalibro.desktop.configuration;

import static org.junit.Assert.*;
import static org.kalibro.MetricFixtures.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.CompoundMetric;
import org.kalibro.Granularity;
import org.kalibro.Language;
import org.kalibro.NativeMetric;
import org.kalibro.desktop.ComponentFinder;
import org.kalibro.desktop.swingextension.field.MaybeEditableField;
import org.kalibro.desktop.swingextension.field.TextField;
import org.kalibro.desktop.swingextension.field.UneditableField;
import org.kalibro.tests.UnitTest;

public class MetricPanelTest extends UnitTest {

	private NativeMetric nativeMetric;
	private CompoundMetric compoundMetric;

	private MetricPanel panel;
	private ComponentFinder finder;

	@Before
	public void setUp() {
		nativeMetric = analizoMetric("loc");
		compoundMetric = sc();
		panel = new MetricPanel();
		finder = new ComponentFinder(panel);
	}

	@Test
	public void shouldGetNativeMetric() {
		panel.set(nativeMetric);
		assertDeepEquals(nativeMetric, panel.get());
	}

	@Test
	public void shouldGetCompoundMetric() {
		nameField().set(compoundMetric.getName());
		scopeField().set(compoundMetric.getScope());
		textField("description").set(compoundMetric.getDescription());
		textField("script").set(compoundMetric.getScript());
		assertDeepEquals(compoundMetric, panel.get());
	}

	@Test
	public void shouldSetNativeMetric() {
		panel.set(nativeMetric);
		assertEquals(nativeMetric.getName(), nameField().get());
		assertEquals(nativeMetric.getScope(), scopeField().get());
		assertEquals(nativeMetric.getDescription(), textField("description").get());
		assertEquals(nativeMetric.getLanguages(), languagesField().get());

		assertFalse(nameField().isEditable());
		assertFalse(scopeField().isEditable());
		finder.assertNotPresent("script");
	}

	@Test
	public void shouldSetCompoundMetric() {
		panel.set(compoundMetric);
		assertEquals(compoundMetric.getName(), nameField().get());
		assertEquals(compoundMetric.getScope(), scopeField().get());
		assertEquals(compoundMetric.getDescription(), textField("description").get());
		assertEquals(compoundMetric.getScript(), textField("script").get());

		assertTrue(nameField().isEditable());
		assertTrue(scopeField().isEditable());
		finder.assertNotPresent("origin");
		finder.assertNotPresent("language");
	}

	private MaybeEditableField<String> nameField() {
		return finder.find("name", MaybeEditableField.class);
	}

	private MaybeEditableField<Granularity> scopeField() {
		return finder.find("scope", MaybeEditableField.class);
	}

	private UneditableField<List<Language>> languagesField() {
		return finder.find("languages", UneditableField.class);
	}

	private TextField textField(String name) {
		return finder.find(name, TextField.class);
	}
}