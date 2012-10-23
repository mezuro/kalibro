package org.kalibro.desktop.configuration;

import java.awt.Component;
import java.util.Collection;

import org.kalibro.*;
import org.kalibro.desktop.swingextension.Label;
import org.kalibro.desktop.swingextension.field.*;
import org.kalibro.desktop.swingextension.panel.EditPanel;
import org.kalibro.desktop.swingextension.panel.GridBagPanelBuilder;

public class MetricPanel extends EditPanel<Metric> {

	private MaybeEditableField<String> nameField;
	private MaybeEditableField<Granularity> scopeField;
	private TextField descriptionField;
	private LanguagesField languagesField;
	private TextField scriptField;

	public MetricPanel() {
		super("metric");
	}

	@Override
	protected void createComponents(Component... innerComponents) {
		nameField = new MaybeEditableField<String>(new StringField("name", 30));
		scopeField = new MaybeEditableField<Granularity>(new ChoiceField<Granularity>("scope", Granularity.values()));
		descriptionField = new TextField("description", 1, 1, "Description");
		languagesField = new LanguagesField();
		scriptField = new TextField("script", 4, 25, "Script");
	}

	@Override
	protected void buildPanel() {
		buildPanel(true);
	}

	private void buildPanel(boolean compound) {
		GridBagPanelBuilder builder = new GridBagPanelBuilder(this);
		builder.add(new Label("Name:"));
		builder.add(nameField, 2);
		builder.newLine();
		builder.add(new Label("Scope:"));
		builder.add(scopeField);
		if (compound) {
			descriptionField.changeSize(3, 10);
			builder.add(scriptField, 1, 3, false);
			builder.newLine();
			builder.add(descriptionField, 2, 2);
		} else {
			descriptionField.changeSize(4, 25);
			builder.add(descriptionField, 1, 3, false);
			builder.newLine();
			builder.addSimpleLine(new Label("Languages:"), languagesField);
		}
	}

	@Override
	public void set(Metric metric) {
		boolean compound = metric.isCompound();
		nameField.set(metric.getName());
		scopeField.set(metric.getScope());
		descriptionField.set(metric.getDescription());
		if (compound)
			showCompoundMetric((CompoundMetric) metric);
		else
			showNativeMetric((NativeMetric) metric);
		nameField.setEditable(compound);
		scopeField.setEditable(compound);
		buildPanel(compound);
	}

	private void showNativeMetric(NativeMetric metric) {
		languagesField.set(metric.getLanguages());
	}

	private void showCompoundMetric(CompoundMetric metric) {
		scriptField.set(metric.getScript());
	}

	@Override
	public Metric get() {
		Metric metric = nameField.isEditable() ? retrieveCompoundMetric() : retrieveNativeMetric();
		metric.setDescription(descriptionField.get());
		return metric;
	}

	private NativeMetric retrieveNativeMetric() {
		String name = nameField.get();
		Granularity scope = scopeField.get();
		Language[] languages = languagesField.get().toArray(new Language[0]);
		NativeMetric metric = new NativeMetric(name, scope, languages);
		return metric;
	}

	private CompoundMetric retrieveCompoundMetric() {
		CompoundMetric metric = new CompoundMetric();
		metric.setName(nameField.get());
		metric.setScope(scopeField.get());
		metric.setScript(scriptField.get());
		return metric;
	}

	private class LanguagesField extends UneditableField<Collection<Language>> {

		LanguagesField() {
			super("languages");
		}

		@Override
		public void set(Collection<Language> languages) {
			super.set(languages);
			setText(languages.toString().replaceAll("[\\[\\]]", ""));
		}
	}
}