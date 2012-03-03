package org.kalibro.desktop.configuration;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.model.CompoundMetric;
import org.kalibro.core.model.CompoundMetricFixtures;
import org.kalibro.core.model.NativeMetric;
import org.kalibro.core.model.NativeMetricFixtures;
import org.kalibro.core.model.enums.Granularity;
import org.kalibro.core.model.enums.Language;
import org.kalibro.desktop.ComponentFinder;
import org.kalibro.desktop.swingextension.field.MaybeEditableField;
import org.kalibro.desktop.swingextension.field.TextField;
import org.kalibro.desktop.swingextension.field.UneditableField;

public class MetricPanelTest extends KalibroTestCase {

	private NativeMetric nativeMetric;
	private CompoundMetric compoundMetric;

	private MetricPanel panel;
	private ComponentFinder finder;

	@Before
	public void setUp() {
		nativeMetric = NativeMetricFixtures.nativeMetric("loc");
		compoundMetric = CompoundMetricFixtures.sc();
		panel = new MetricPanel();
		finder = new ComponentFinder(panel);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldShowNativeMetric() {
		panel.show(nativeMetric);
		assertEquals(nativeMetric.getName(), nameField().get());
		assertEquals(nativeMetric.getScope(), scopeField().get());
		assertEquals(nativeMetric.getDescription(), textField("description").get());
		assertEquals(nativeMetric.getOrigin(), originField().get());
		assertEquals(nativeMetric.getLanguages(), languagesField().get());

		assertFalse(nameField().isEditable());
		assertFalse(scopeField().isEditable());
		finder.assertNotPresent("script");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldShowCompoundMetric() {
		panel.show(compoundMetric);
		assertEquals(compoundMetric.getName(), nameField().get());
		assertEquals(compoundMetric.getScope(), scopeField().get());
		assertEquals(compoundMetric.getDescription(), textField("description").get());
		assertEquals(compoundMetric.getScript(), textField("script").get());

		assertTrue(nameField().isEditable());
		assertTrue(scopeField().isEditable());
		finder.assertNotPresent("origin");
		finder.assertNotPresent("language");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRetrieveNativeMetric() {
		panel.show(nativeMetric);
		assertDeepEquals(nativeMetric, panel.get());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRetrieveCompoundMetric() {
		nameField().set(compoundMetric.getName());
		scopeField().set(compoundMetric.getScope());
		textField("description").set(compoundMetric.getDescription());
		textField("script").set(compoundMetric.getScript());
		assertDeepEquals(compoundMetric, panel.get());
	}

	private MaybeEditableField<String> nameField() {
		return finder.find("name", MaybeEditableField.class);
	}

	private MaybeEditableField<Granularity> scopeField() {
		return finder.find("scope", MaybeEditableField.class);
	}

	private UneditableField<String> originField() {
		return finder.find("origin", UneditableField.class);
	}

	private UneditableField<List<Language>> languagesField() {
		return finder.find("languages", UneditableField.class);
	}

	private TextField textField(String name) {
		return finder.find(name, TextField.class);
	}
}