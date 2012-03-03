package org.kalibro.desktop.configuration;

import java.util.List;

import org.kalibro.core.model.CompoundMetric;
import org.kalibro.core.model.Metric;
import org.kalibro.core.model.NativeMetric;
import org.kalibro.core.model.enums.Granularity;
import org.kalibro.core.model.enums.Language;
import org.kalibro.desktop.swingextension.Label;
import org.kalibro.desktop.swingextension.field.*;
import org.kalibro.desktop.swingextension.panel.EditPanel;
import org.kalibro.desktop.swingextension.panel.GridBagPanelBuilder;

public class MetricPanel extends EditPanel<Metric> {

	private MaybeEditableField<String> nameField;
	private MaybeEditableField<Granularity> scopeField;
	private TextField descriptionField;
	private UneditableField<String> originField;
	private UneditableField<List<Language>> languagesField;
	private TextField scriptField;

	public MetricPanel() {
		super("metric");
	}

	@Override
	protected void createComponents() {
		nameField = new MaybeEditableField<String>(new StringField("name", 30));
		scopeField = new MaybeEditableField<Granularity>(new ChoiceField<Granularity>("scope", Granularity.values()));
		descriptionField = new TextField("description", 1, 1, "Description");
		originField = new UneditableField<String>("origin");
		languagesField = new UneditableField<List<Language>>("languages");
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
			builder.addSimpleLine(new Label("Origin:"), originField);
		}
	}

	@Override
	public void show(Metric metric) {
		boolean compound = metric.isCompound();
		nameField.setValue(metric.getName());
		scopeField.setValue(metric.getScope());
		descriptionField.setValue(metric.getDescription());
		if (compound)
			showCompoundMetric((CompoundMetric) metric);
		else
			showNativeMetric((NativeMetric) metric);
		nameField.setEditable(compound);
		scopeField.setEditable(compound);
		buildPanel(compound);
	}

	private void showNativeMetric(NativeMetric metric) {
		originField.setValue(metric.getOrigin());
		languagesField.setValue(metric.getLanguages());
	}

	private void showCompoundMetric(CompoundMetric metric) {
		scriptField.setValue(metric.getScript());
	}

	@Override
	public Metric get() {
		Metric metric = nameField.isEditable() ? retrieveCompoundMetric() : retrieveNativeMetric();
		metric.setDescription(descriptionField.getValue());
		return metric;
	}

	private NativeMetric retrieveNativeMetric() {
		String name = nameField.getValue();
		Granularity scope = scopeField.getValue();
		List<Language> languages = languagesField.getValue();
		NativeMetric metric = new NativeMetric(name, scope, languages);
		metric.setOrigin(originField.getValue());
		return metric;
	}

	private CompoundMetric retrieveCompoundMetric() {
		CompoundMetric metric = new CompoundMetric();
		metric.setName(nameField.getValue());
		metric.setScope(scopeField.getValue());
		metric.setScript(scriptField.getValue());
		return metric;
	}
}